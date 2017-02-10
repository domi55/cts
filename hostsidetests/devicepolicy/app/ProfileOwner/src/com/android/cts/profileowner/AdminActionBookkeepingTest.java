/*
 * Copyright (C) 2017 The Android Open Source Project
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
 * limitations under the License.
 */
package com.android.cts.profileowner;

import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;

import com.android.org.conscrypt.TrustedCertificateStore;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

public class AdminActionBookkeepingTest extends BaseProfileOwnerTest {
    /*
     * The CA cert below is the content of cacert.pem as generated by:
     *
     * openssl req -new -x509 -days 3650 -extensions v3_ca -keyout cakey.pem -out cacert.pem
     */
    private static final String TEST_CA =
            "-----BEGIN CERTIFICATE-----\n" +
            "MIIDXTCCAkWgAwIBAgIJAK9Tl/F9V8kSMA0GCSqGSIb3DQEBCwUAMEUxCzAJBgNV\n" +
            "BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBX\n" +
            "aWRnaXRzIFB0eSBMdGQwHhcNMTUwMzA2MTczMjExWhcNMjUwMzAzMTczMjExWjBF\n" +
            "MQswCQYDVQQGEwJBVTETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50\n" +
            "ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\n" +
            "CgKCAQEAvItOutsE75WBTgTyNAHt4JXQ3JoseaGqcC3WQij6vhrleWi5KJ0jh1/M\n" +
            "Rpry7Fajtwwb4t8VZa0NuM2h2YALv52w1xivql88zce/HU1y7XzbXhxis9o6SCI+\n" +
            "oVQSbPeXRgBPppFzBEh3ZqYTVhAqw451XhwdA4Aqs3wts7ddjwlUzyMdU44osCUg\n" +
            "kVg7lfPf9sTm5IoHVcfLSCWH5n6Nr9sH3o2ksyTwxuOAvsN11F/a0mmUoPciYPp+\n" +
            "q7DzQzdi7akRG601DZ4YVOwo6UITGvDyuAAdxl5isovUXqe6Jmz2/myTSpAKxGFs\n" +
            "jk9oRoG6WXWB1kni490GIPjJ1OceyQIDAQABo1AwTjAdBgNVHQ4EFgQUH1QIlPKL\n" +
            "p2OQ/AoLOjKvBW4zK3AwHwYDVR0jBBgwFoAUH1QIlPKLp2OQ/AoLOjKvBW4zK3Aw\n" +
            "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAcMi4voMMJHeQLjtq8Oky\n" +
            "Azpyk8moDwgCd4llcGj7izOkIIFqq/lyqKdtykVKUWz2bSHO5cLrtaOCiBWVlaCV\n" +
            "DYAnnVLM8aqaA6hJDIfaGs4zmwz0dY8hVMFCuCBiLWuPfiYtbEmjHGSmpQTG6Qxn\n" +
            "ZJlaK5CZyt5pgh5EdNdvQmDEbKGmu0wpCq9qjZImwdyAul1t/B0DrsWApZMgZpeI\n" +
            "d2od0VBrCICB1K4p+C51D93xyQiva7xQcCne+TAnGNy9+gjQ/MyR8MRpwRLv5ikD\n" +
            "u0anJCN8pXo6IMglfMAsoton1J6o5/ae5uhC6caQU8bNUsCK570gpNfjkzo6rbP0\n" +
            "wQ==\n" +
            "-----END CERTIFICATE-----";

    @Override
    protected void tearDown() throws Exception {
        mDevicePolicyManager.uninstallCaCert(getWho(), TEST_CA.getBytes());

        super.tearDown();
    }

    /**
     * Test: It should be recored whether the Profile Owner or the user set the current IME.
     */
    public void testIsDefaultInputMethodSet() throws Exception {
        final String setting = Settings.Secure.DEFAULT_INPUT_METHOD;
        final ContentResolver resolver = getContext().getContentResolver();
        final String ime = Settings.Secure.getString(resolver, setting);

        Settings.Secure.putString(resolver, setting, "com.test.1");
        Thread.sleep(500);
        assertFalse(mDevicePolicyManager.isCurrentInputMethodSetByOwner());

        mDevicePolicyManager.setSecureSetting(getWho(), setting, "com.test.2");
        Thread.sleep(500);
        assertTrue(mDevicePolicyManager.isCurrentInputMethodSetByOwner());

        Settings.Secure.putString(resolver, setting, ime);
        Thread.sleep(500);
        assertFalse(mDevicePolicyManager.isCurrentInputMethodSetByOwner());
    }

    /**
     * Test: It should be recored whether the Profile Owner or the user installed a CA cert.
     */
    public void testGetPolicyInstalledCaCerts() throws Exception {
        final byte[] rawCert = TEST_CA.getBytes();
        final Certificate cert = CertificateFactory.getInstance("X.509")
                .generateCertificate(new ByteArrayInputStream(rawCert));
        final TrustedCertificateStore store = new TrustedCertificateStore();

        // Install a CA cert.
        assertNull(store.getCertificateAlias(cert));
        assertTrue(mDevicePolicyManager.installCaCert(getWho(), rawCert));
        final String alias = store.getCertificateAlias(cert);
        assertNotNull(alias);

        // Verify that the CA cert was marked as installed by the Profile Owner.
        verifyOwnerInstalledStatus(alias, true);

        // Uninstall the CA cert.
        mDevicePolicyManager.uninstallCaCert(getWho(), rawCert);

        // Verify that the CA cert is no longer marked as installed by the Profile Owner.
        verifyOwnerInstalledStatus(alias, false);
    }

    private void verifyOwnerInstalledStatus(String alias, boolean expectOwnerInstalled) {
        final List<String> ownerInstalledCerts =
                mDevicePolicyManager.getOwnerInstalledCaCerts(Process.myUserHandle());
        assertNotNull(ownerInstalledCerts);
        assertEquals(expectOwnerInstalled, ownerInstalledCerts.contains(alias));
    }
}
