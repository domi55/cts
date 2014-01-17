/*
 * Copyright (C) 2014 The Android Open Source Project
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

package android.print.cts;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;

import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintAttributes.Resolution;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.print.cts.services.FirstPrintService;
import android.print.cts.services.PrintServiceCallbacks;
import android.print.cts.services.PrinterDiscoverySessionCallbacks;
import android.print.cts.services.SecondPrintService;
import android.print.cts.services.StubbablePrinterDiscoverySession;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

/**
 * This test verifies that the system correctly adjust the
 * page ranges to be printed depending whether the app gave
 * the requested pages, more pages, etc.
 */
public class PageRangeAdjustmentTest extends BasePrintTest {

    private static final String FIRST_PRINTER = "First printer";

    public void testAllPagesWantedAndAllPagesWritten() throws Exception {
        // Create a callback for the target print service.
        PrintServiceCallbacks firstServiceCallbacks = createMockPrintServiceCallbacks(
            new Answer<PrinterDiscoverySessionCallbacks>() {
            @Override
            public PrinterDiscoverySessionCallbacks answer(InvocationOnMock invocation) {
                    return createMockFirstPrinterDiscoverySessionCallbacks();
                }
            },
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                PrintJob printJob = (PrintJob) invocation.getArguments()[0];
                PageRange[] pages = printJob.getInfo().getPages();
                assert(pages.length == 1 && PageRange.ALL_PAGES.equals(pages[0]));
                printJob.complete();
                onPrintJobQueuedCalled();
                return null;
            }
        }, null);

        // Configure the print services.
        FirstPrintService.setCallbacks(firstServiceCallbacks);
        SecondPrintService.setCallbacks(createSecondMockPrintServiceCallbacks());

        // Create a mock print adapter.
        final PrintDocumentAdapter adapter = createMockPrintDocumentAdapter(
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LayoutResultCallback callback = (LayoutResultCallback) invocation.getArguments()[3];
                PrintDocumentInfo info = new PrintDocumentInfo.Builder(PRINT_JOB_NAME)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(3)
                        .build();
                callback.onLayoutFinished(info, false);
                // Mark layout was called.
                onLayoutCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                PageRange[] pages = (PageRange[]) args[0];
                ParcelFileDescriptor fd = (ParcelFileDescriptor) args[1];
                WriteResultCallback callback = (WriteResultCallback) args[3];
                fd.close();
                callback.onWriteFinished(pages);
                // Mark write was called.
                onWriteCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mark finish was called.
                onFinishCalled();
                return null;
            }
        });

        // Start printing.
        print(adapter);

        // Wait for write.
        waitForWriteForAdapterCallback();

        // Select the first printer.
        selectPrinter(FIRST_PRINTER);

        // Wait for layout as the printer has different capabilities.
        waitForLayoutAdapterCallbackCount(2);

        // Click the print button.
        clickPrintButton();

        // Wait for finish.
        waitForAdapterFinishCallbackCalled();

        // Wait for the print job.
        waitForServiceOnPrintJobQueuedCallbackCalled();

        // Verify the expected calls.
        InOrder inOrder = inOrder(firstServiceCallbacks);

        // We create a new session first.
        inOrder.verify(firstServiceCallbacks)
                .onCreatePrinterDiscoverySessionCallbacks();

        // Next we wait for a call with the print job.
        inOrder.verify(firstServiceCallbacks).onPrintJobQueued(
                any(PrintJob.class));
    }

    public void testSomePagesWantedAndAllPagesWritten() throws Exception {
        // Create a callback for the target print service.
        PrintServiceCallbacks firstServiceCallbacks = createMockPrintServiceCallbacks(
            new Answer<PrinterDiscoverySessionCallbacks>() {
            @Override
            public PrinterDiscoverySessionCallbacks answer(InvocationOnMock invocation) {
                    return createMockFirstPrinterDiscoverySessionCallbacks();
                }
            },
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                PrintJob printJob = (PrintJob) invocation.getArguments()[0];
                PageRange[] pages = printJob.getInfo().getPages();
                // We always as for the first page for preview and in this
                // case we write all, i.e. more that needed.
                assertTrue(pages.length == 1 && pages[0].getStart() == 1
                        && pages[0].getEnd() == 1);
                printJob.complete();
                onPrintJobQueuedCalled();
                return null;
            }
        }, null);

        // Configure the print services.
        FirstPrintService.setCallbacks(firstServiceCallbacks);
        SecondPrintService.setCallbacks(createSecondMockPrintServiceCallbacks());

        // Create a mock print adapter.
        final PrintDocumentAdapter adapter = createMockPrintDocumentAdapter(
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LayoutResultCallback callback = (LayoutResultCallback) invocation.getArguments()[3];
                PrintDocumentInfo info = new PrintDocumentInfo.Builder(PRINT_JOB_NAME)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(3)
                        .build();
                callback.onLayoutFinished(info, false);
                // Mark layout was called.
                onLayoutCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ParcelFileDescriptor fd = (ParcelFileDescriptor) args[1];
                WriteResultCallback callback = (WriteResultCallback) args[3];
                fd.close();
                callback.onWriteFinished(new PageRange[] {PageRange.ALL_PAGES});
                // Mark write was called.
                onWriteCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mark finish was called.
                onFinishCalled();
                return null;
            }
        });

        // Start printing.
        print(adapter);

        // Wait for write.
        waitForWriteForAdapterCallback();

        // Select the first printer.
        selectPrinter(FIRST_PRINTER);

        // Wait for layout as the printer has different capabilities.
        waitForLayoutAdapterCallbackCount(2);

        // Select only the second page.
        selectPages("2");

        // Click the print button.
        clickPrintButton();

        // Wait for finish.
        waitForAdapterFinishCallbackCalled();

        // Wait for the print job.
        waitForServiceOnPrintJobQueuedCallbackCalled();

        // Verify the expected calls.
        InOrder inOrder = inOrder(firstServiceCallbacks);

        // We create a new session first.
        inOrder.verify(firstServiceCallbacks)
                .onCreatePrinterDiscoverySessionCallbacks();

        // Next we wait for a call with the print job.
        inOrder.verify(firstServiceCallbacks).onPrintJobQueued(
                any(PrintJob.class));
    }

    public void testSomePagesWantedAndSomeMorePagesWritten() throws Exception {
        // Create a callback for the target print service.
        PrintServiceCallbacks firstServiceCallbacks = createMockPrintServiceCallbacks(
            new Answer<PrinterDiscoverySessionCallbacks>() {
            @Override
            public PrinterDiscoverySessionCallbacks answer(InvocationOnMock invocation) {
                    return createMockFirstPrinterDiscoverySessionCallbacks();
                }
            },
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                PrintJob printJob = (PrintJob) invocation.getArguments()[0];
                PageRange[] pages = printJob.getInfo().getPages();
                assert(pages.length == 1 && pages[0].getStart() == 1
                        && pages[0].getEnd() == 2);
                printJob.complete();
                onPrintJobQueuedCalled();
                return null;
            }
        }, null);

        // Configure the print services.
        FirstPrintService.setCallbacks(firstServiceCallbacks);
        SecondPrintService.setCallbacks(createSecondMockPrintServiceCallbacks());

        // Create a mock print adapter.
        final PrintDocumentAdapter adapter = createMockPrintDocumentAdapter(
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LayoutResultCallback callback = (LayoutResultCallback) invocation.getArguments()[3];
                PrintDocumentInfo info = new PrintDocumentInfo.Builder(PRINT_JOB_NAME)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(3)
                        .build();
                callback.onLayoutFinished(info, false);
                // Mark layout was called.
                onLayoutCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                PageRange[] pages = (PageRange[]) args[0];
                ParcelFileDescriptor fd = (ParcelFileDescriptor) args[1];
                WriteResultCallback callback = (WriteResultCallback) args[3];
                // We expect a single range as it is either the first page
                // or the page we selected in the UI.
                assertSame(pages.length, 1);
                fd.close();

                PageRange reqeustedPages = pages[0];
                if (reqeustedPages.getStart() == reqeustedPages.getEnd()
                        && reqeustedPages.getEnd() == 0) {
                    // If asked for the first page, which is for preview
                    // then write it...
                    callback.onWriteFinished(pages);
                } else {
                    // otherwise write a page more that the one we selected.
                    callback.onWriteFinished(new PageRange[] {new PageRange(2, 3)});
                }
                // Mark write was called.
                onWriteCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mark finish was called.
                onFinishCalled();
                return null;
            }
        });

        // Start printing.
        print(adapter);

        // Wait for write.
        waitForWriteForAdapterCallback();

        // Select the first printer.
        selectPrinter(FIRST_PRINTER);

        // Wait for layout as the printer has different capabilities.
        waitForLayoutAdapterCallbackCount(2);

        // Select only the third page.
        selectPages("3");

        // Click the print button.
        clickPrintButton();

        // Wait for finish.
        waitForAdapterFinishCallbackCalled();

        // Wait for the print job.
        waitForServiceOnPrintJobQueuedCallbackCalled();

        // Verify the expected calls.
        InOrder inOrder = inOrder(firstServiceCallbacks);

        // We create a new session first.
        inOrder.verify(firstServiceCallbacks)
                .onCreatePrinterDiscoverySessionCallbacks();

        // Next we wait for a call with the print job.
        inOrder.verify(firstServiceCallbacks).onPrintJobQueued(
                any(PrintJob.class));
    }

    public void testSomePagesWantedAndNotWritten() throws Exception {
        // Create a callback for the target print service.
        PrintServiceCallbacks firstServiceCallbacks = createMockPrintServiceCallbacks(
            new Answer<PrinterDiscoverySessionCallbacks>() {
            @Override
            public PrinterDiscoverySessionCallbacks answer(InvocationOnMock invocation) {
                    return createMockFirstPrinterDiscoverySessionCallbacks();
                }
            },
            null, null);

        // Configure the print services.
        FirstPrintService.setCallbacks(firstServiceCallbacks);
        SecondPrintService.setCallbacks(createSecondMockPrintServiceCallbacks());

        // Create a mock print adapter.
        final PrintDocumentAdapter adapter = createMockPrintDocumentAdapter(
            new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                LayoutResultCallback callback = (LayoutResultCallback) invocation.getArguments()[3];
                PrintDocumentInfo info = new PrintDocumentInfo.Builder(PRINT_JOB_NAME)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(3)
                        .build();
                callback.onLayoutFinished(info, false);
                // Mark layout was called.
                onLayoutCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                PageRange[] pages = (PageRange[]) args[0];
                ParcelFileDescriptor fd = (ParcelFileDescriptor) args[1];
                WriteResultCallback callback = (WriteResultCallback) args[3];
                assertSame(pages.length, 1);
                fd.close();

                // We should be asked for the first page...
                assertSame(pages[0].getStart(), 0);
                assertSame(pages[0].getEnd(), 0);

                // ...just write a the wring page.
                callback.onWriteFinished(new PageRange[] {new PageRange(1, 1)});

                // Mark write was called.
                onWriteCalled();
                return null;
            }
        }, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                // Mark finish was called.
                onFinishCalled();
                return null;
            }
        });

        // Start printing.
        print(adapter);

        // Wait for write.
        waitForWriteForAdapterCallback();

        // Cancel printing.
        UiDevice.getInstance().pressBack(); // wakes up the device.
        UiDevice.getInstance().pressBack();

        // Wait for finish.
        waitForAdapterFinishCallbackCalled();

        // Verify the expected calls.
        InOrder inOrder = inOrder(firstServiceCallbacks);

        // We create a new session first.
        inOrder.verify(firstServiceCallbacks)
                .onCreatePrinterDiscoverySessionCallbacks();

        // We should not receive a print job callback.
        inOrder.verify(firstServiceCallbacks, never()).onPrintJobQueued(
                any(PrintJob.class));
    }

    private void selectPages(String pages) throws UiObjectNotFoundException {
        UiObject pagesSpinner = new UiObject(new UiSelector().resourceId(
                "com.android.printspooler:id/range_options_spinner"));
        pagesSpinner.click();

        UiObject rangeOption = new UiObject(new UiSelector().text("Range"));
        rangeOption.click();

        UiObject pagesEditText = new UiObject(new UiSelector().resourceId(
                "com.android.printspooler:id/page_range_edittext"));
        pagesEditText.setText(pages);
    }

    private PrinterDiscoverySessionCallbacks createMockFirstPrinterDiscoverySessionCallbacks() {
        return createMockPrinterDiscoverySessionCallbacks(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                PrinterDiscoverySessionCallbacks mock = (PrinterDiscoverySessionCallbacks)
                        invocation.getMock();

                StubbablePrinterDiscoverySession session = mock.getSession();
                PrintService service = session.getService();

                if (session.getPrinters().isEmpty()) {
                          List<PrinterInfo> printers = new ArrayList<PrinterInfo>();

                    // Add one printer.
                    PrinterId firstPrinterId = service.generatePrinterId("first_printer");
                    PrinterCapabilitiesInfo firstCapabilities =
                            new PrinterCapabilitiesInfo.Builder(firstPrinterId)
                        .setMinMargins(new Margins(200, 200, 200, 200))
                        .addMediaSize(MediaSize.ISO_A4, true)
                        .addMediaSize(MediaSize.ISO_A5, false)
                        .addResolution(new Resolution("300x300", "300x300", 300, 300), true)
                        .setColorModes(PrintAttributes.COLOR_MODE_COLOR,
                                PrintAttributes.COLOR_MODE_COLOR)
                        .build();
                    PrinterInfo firstPrinter = new PrinterInfo.Builder(firstPrinterId,
                            FIRST_PRINTER, PrinterInfo.STATUS_IDLE)
                        .setCapabilities(firstCapabilities)
                        .build();
                    printers.add(firstPrinter);

                    session.addPrinters(printers);
                }

                return null;
            }
        }, null, null, null, null, null);
    }

    private PrintServiceCallbacks createSecondMockPrintServiceCallbacks() {
        return createMockPrintServiceCallbacks(null, null, null);
    }
}
