package com.example.kevin.vendollarv3;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.EddystoneScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.EddystoneNamespace;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.EventType;
import com.kontakt.sdk.android.ble.filter.eddystone.EddystoneFilters;
import com.kontakt.sdk.android.ble.filter.eddystone.UIDFilter;
import com.kontakt.sdk.android.ble.filter.eddystone.URLFilter;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.spec.EddystoneFrameType;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.model.IDevice;
import com.kontakt.sdk.android.common.profile.DeviceProfile;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.kontakt.sdk.android.common.util.Constants;
import com.kontakt.sdk.android.http.HttpResult;
import com.kontakt.sdk.android.http.KontaktApiClient;
import com.kontakt.sdk.android.http.RequestDescription;
import com.kontakt.sdk.android.http.exception.ClientException;
import com.kontakt.sdk.android.http.interfaces.ResultApiCallback;
import com.kontakt.sdk.android.manager.KontaktProximityManager;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProximityManager.ProximityListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProximityManagerContract proximityManager;
    String beaconString;
    int price;
    boolean foundCount = true;
    ArrayList<String> couponList = new ArrayList<String>();
    private static Context context;
    private Activity activity = (Activity) this;

    public CustomListAdapter adapter;
    public ListView lv;

    static final String COUPON_NAME = "name";
    static final String COUPONE_IMG = "image";

    public ArrayList<String> couponName = new ArrayList<String>();
    public ArrayList<Integer> couponImg= new ArrayList<Integer>();
    public ArrayList<String> couponDesc = new ArrayList<String>();
    private View b;
    //Context context = (Context)this;

    //CharSequence text = new StringBuffer("----------");

    //ScanResult scanResult = new ScanResult();
    Collection<EddystoneFrameType> eddystoneFrameTypes = Arrays.asList(
            EddystoneFrameType.UID,
            EddystoneFrameType.URL,
            EddystoneFrameType.TLM
    );

    //accept Eddystone devices only with specified url addresses
    //List<URLFilter> URLfilterList = Arrays.asList(
    //        EddystoneFilters.newURLFilter("http://google.com"),
    //        EddystoneFilters.newURLFilter("http://kontakt.io")
    //);

    /*
    List<UIDFilter> UIDfilterList = Arrays.asList(
            EddystoneFilters.newInstanceIdFilter("34386f7a5058"),
            EddystoneFilters.newInstanceIdFilter("687571387930"),
            EddystoneFilters.newInstanceIdFilter("595241546637"),
            EddystoneFilters.newInstanceIdFilter("7a484e62464d"),
            EddystoneFilters.newNamespaceIdFilter("f7826da6bc5b71e0893e")
    );*/

    //Collection<IEddystoneNamespace> eddystoneNamespaces = Arrays.asList(
    //        (IEddystoneNamespace) new EddystoneNamespace("f7826da6bc5b71e0893e","namespace 1")
    //);

    EddystoneScanContext eddystoneScanContext = new EddystoneScanContext.Builder()
            .setEventTypes(Arrays.asList(
                    EventType.SPACE_ENTERED,
                    EventType.SPACE_ABANDONED,
                    EventType.DEVICE_LOST,
                    EventType.DEVICE_DISCOVERED))
            .setTriggerFrameTypes(eddystoneFrameTypes)
                    //.setEddystoneNamespaces(eddystoneNamespaces)
            //.setUIDFilters(UIDfilterList)
            //.setURLFilters(URLfilterList)
            .build();



    //Collection<IEddystoneNamespace> eddystoneNamespaces = new ArrayList<>();
    //eddystoneNamespaces.add(new EddystoneNamespace("f7826da64fa24e988024","namespace 1"));
    //eddystoneNamespaces.add(new EddystoneNamespace("2b17b17d1dea47d1a690","namespace 2"));

    private ScanContext scanContext;
    //ScanContext scanContext = new ScanContext.Builder()
    //        .setEddystoneScanContext(eddystoneScanContext)
    //        .build();

    public Dialog gimmePopuzz(final boolean whichCoupon) {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view;

        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Would you like this coupon?");

        // Set the button options
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv = (TextView)findViewById(R.id.textView2);

                // User Accepted the coupon
                if (whichCoupon) {
                    couponName.add("Western NC Farms");
                    couponImg.add(R.drawable.coupon2);
                    couponDesc.add("$2.00 off!");

                    price += 2;
                    tv.setText("Total Saved: $" + price + ".00");
                    tv.invalidate();
                } else {
                    couponName.add("Grace Hill Farms");
                    couponImg.add(R.drawable.coupon1);
                    couponDesc.add("$1.00 off!");

                    price += 1;
                    tv.setText("Total Saved: $" + price + ".00");
                    tv.invalidate();
                }

                lv = (ListView) findViewById(R.id.couponListView);
                adapter = new CustomListAdapter(activity, couponName, couponImg, couponDesc);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User declined the coupon
            }
        });

        LayoutInflater factory = LayoutInflater.from(this);

        if (whichCoupon) {
            view = factory.inflate(R.layout.content_coupon_popup2, null);
        }
        else {
            view = factory.inflate(R.layout.content_coupon_popup1, null);
        }

        builder.setView(view);

        // Get and return the AlertDialog from create()
        return builder.create();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KontaktSDK.initialize("wvFaZmilypJIauSMJMYDQEDCekHhoyTc");
        ConnectActivity connectActivity = new ConnectActivity();
        proximityManager = new KontaktProximityManager(this);
        MainActivity.context = getApplicationContext();

        b = findViewById(R.id.imageButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, b);

                // Inflating the popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                // Registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        LayoutInflater inflater = getLayoutInflater();
                        final View view;
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.home:
                                //intent = new Intent(context, MainActivity.class);
                                //startActivity(intent);
                                return true;
                            case R.id.recipes:
                                intent = new Intent(context, page1.class);
                                startActivity(intent);
                                return true;
                            case R.id.fotw:
                                intent = new Intent(context, page2.class);
                                startActivity(intent);
                                return true;
                            case R.id.checkout:
                                intent = new Intent(context, page3.class);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }

                    }
                });

                // Showing the popup menu
                popup.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        proximityManager.initializeScan(getScanContext(), new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(MainActivity.this);

                checkPermissionAndStart();
            }

            @Override
            public void onConnectionFailure() {

            }
        });
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(beaconString);
        tv.invalidate();
    }
    */

    @Override
    protected void onStop() {
        super.onStop();
        proximityManager.detachListener(this);
        proximityManager.disconnect();
    }


    private ScanContext getScanContext() {
        if (scanContext == null) {
            Log.d(TAG, "TEST");
            scanContext = new ScanContext.Builder()
                    .setScanPeriod(ScanPeriod.RANGING) // or for monitoring for 15 seconds scan and 10 seconds waiting:
                            //.setScanPeriod(new ScanPeriod(TimeUnit.SECONDS.toMillis(15), TimeUnit.SECONDS.toMillis(10)))
                    .setScanMode(ProximityManager.SCAN_MODE_BALANCED)
                    .setActivityCheckConfiguration(ActivityCheckConfiguration.MINIMAL)
                    .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                    //.setIBeaconScanContext(new IBeaconScanContext.Builder().build())
                    //.setEddystoneScanContext(new EddystoneScanContext.Builder().build())
                    .setEddystoneScanContext(eddystoneScanContext)
                    .setForceScanConfiguration(ForceScanConfiguration.MINIMAL)
                    .build();
        }
        return scanContext;
    }

    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {
        Log.d(TAG, "EVENT?");

        List<? extends RemoteBluetoothDevice> deviceList = bluetoothDeviceEvent.getDeviceList();
        long timestamp = bluetoothDeviceEvent.getTimestamp();
        DeviceProfile deviceProfile = bluetoothDeviceEvent.getDeviceProfile();
        LinearLayout layout = new LinearLayout(getApplicationContext());
        TextView tv = (TextView)findViewById(R.id.textView);
        //EditText et = new EditText(getApplicationContext());
        boolean beaconLost = false;
        //EditText et2 = new EditText(findViewById(R.id.textView));

        switch (bluetoothDeviceEvent.getEventType()) {
            case SPACE_ENTERED:
                beaconString = "In Beacon Range";
                //tv.setText(beaconString);
                tv.invalidate();
                Log.d(TAG, "namespace or region entered");
                break;
            case DEVICE_DISCOVERED:
                beaconString = "Found Beacon ";
                tv.setText(beaconString);
                tv.invalidate();
                Log.d(TAG, "found new beacon");

                //Coupon
                if(foundCount) {
                    Dialog couponDialog = gimmePopuzz(true);
                    couponDialog.show();
                    foundCount = false;
                }
                else {
                    Dialog couponDialog = gimmePopuzz(false);
                    couponDialog.show();
                    foundCount = true;
                }
                break;
            case DEVICES_UPDATE:
                Log.d(TAG, "updated beacons");
                break;
            //case DEVICE_LOST:
            //    text = new StringBuffer("Lost Device");
            //    tv.setText(text);
            //    tv.invalidate();
            //    et1.setText("Lost Device");
            //    Log.d(TAG, "lost device");
            //    break;
            //case SPACE_ABANDONED:
            //    text = new StringBuffer("Out of Beacon range");
            //    tv.setText(text);
            //    tv.invalidate();
            //    et1.setText("Left Beacon Range");
            //    Log.d(TAG, "namespace or region abandoned");
            //    break;

            default:
                beaconLost = true;
                beaconString = "beacon lost";
                tv.setText(beaconString);
                tv.invalidate();
                Log.d(TAG, "beacon lost");
                break;
            }

        if(beaconLost) {
            beaconString = "beacon lost";
            tv.setText(beaconString);
            tv.invalidate();
            Log.d(TAG, "beacon lost?");
        }
    }

    @Override
    public void onScanStart() {
        Log.d(TAG, "scan started");

    }

    @Override
    public void onScanStop() {
        Log.d(TAG, "scan stopped");
    }

    private void checkPermissionAndStart() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED == checkSelfPermissionResult) {
        //if (false) {
            //already granted
            startScan();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //we should show some explanation for user here
                //showExplanationDialog();
            } else {
                //request permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (100 == requestCode) {
                //same request code as was in request permission
                startScan();
            }

        } else {
            //not granted permission
            //show some explanation dialog that some features will not work
        }
    }

    private void startScan() {
        proximityManager.initializeScan(getScanContext(), new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(MainActivity.this);
            }

            @Override
            public void onConnectionFailure() {

            }
        });
    }

}
