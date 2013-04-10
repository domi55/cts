/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package android.provider.cts.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.cts.StaticAccountAuthenticator;

import java.util.List;

/**
 * Convenience methods for operating on the RawContacts table.
 */
public class RawContactUtil {

    private static final Uri URI = ContactsContract.RawContacts.CONTENT_URI;

    public static void update(ContentResolver resolver, long rawContactId,
            ContentValues values) {
        Uri uri = ContentUris.withAppendedId(URI, rawContactId);
        resolver.update(uri, values, null, null);
    }

    public static long createRawContactWithName(ContentResolver resolver) {
        Long rawContactId = insertRawContact(resolver);
        DataUtil.insertName(resolver, rawContactId);
        return rawContactId;
    }

    public static long insertRawContact(ContentResolver resolver) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.RawContacts.ACCOUNT_NAME, StaticAccountAuthenticator.NAME);
        values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, StaticAccountAuthenticator.TYPE);
        Uri uri = resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        return ContentUris.parseId(uri);
    }

    public static String[] queryByRawContactId(ContentResolver resolver,
            long rawContactId, String[] projection) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                rawContactId);
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        return CommonDatabaseUtils.singleRecordToArray(cursor);
    }

    /**
     * Returns a list of raw contact records.
     *
     * @return A list of records.  Where each record is represented as an array of strings.
     */
    public static List<String[]> queryByContactId(ContentResolver resolver, long contactId,
            String[] projection) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId);
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        return CommonDatabaseUtils.multiRecordToArray(cursor);
    }

    public static void delete(ContentResolver resolver, long rawContactId,
            boolean isSyncAdapter) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId)
                .buildUpon()
                .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, isSyncAdapter + "")
                .build();
        resolver.delete(uri, null, null);
    }

    public static long queryContactIdByRawContactId(ContentResolver resolver, long rawContactid) {
        String[] projection = new String[]{
                ContactsContract.RawContacts.CONTACT_ID
        };
        String[] result = RawContactUtil.queryByRawContactId(resolver, rawContactid,
                projection);
        if (result == null) {
            return CommonDatabaseUtils.NOT_FOUND;
        }
        return Long.parseLong(result[0]);
    }

    public static boolean rawContactExistsById(ContentResolver resolver, long rawContactid) {
        long contactId = queryContactIdByRawContactId(resolver, rawContactid);
        return contactId != CommonDatabaseUtils.NOT_FOUND;
    }
}
