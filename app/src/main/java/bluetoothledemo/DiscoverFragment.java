package bluetoothledemo;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DiscoverFragment extends Fragment {

    private final static String TAG = "DiscoverFragment";
    TextView logger;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Button discover;
    private Boolean discovering = false;
    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if (result == null || result.getDevice() == null) {
                logthis("The data result is empty or no data");
            } else {
                if (result.getDevice().getName() != null) {
                    if (result.getDevice().getName().contains("TM")) {
//                        //name
//                        StringBuilder builder = new StringBuilder("Name: ").append(result.getDevice().getName());
//                        //address
//                        builder.append("\n").append("address: ").append(result.getDevice().getAddress());
                        //data
                        byte[] dataBlock = result.getScanRecord().getBytes();
                        // logthis(Arrays.toString(dataBlock));
                        int temp = (dataBlock[23] & 0xFF) - 43;
                        int pressure = (((dataBlock[24] & 0xFF) & 0x70) << 4) | (dataBlock[22] & 0xFF);

//                        logthis("temp: "+temp + " in C");
                        logthis("temp: " + ((temp * 1.8) + 32) + " in F");
//                        logthis("pressure: " + pressure + "in kpa");
                        logthis("pressure: " + (pressure / 6.895) + "in psi");
//                        String UUIDx = UUID.nameUUIDFromBytes(result.getScanRecord().getBytes()).toString();
//                        logthis("UUID:" +UUIDx);

                        Tire tire = new Tire(result.getDevice().getAddress(), temp, temp, pressure, pressure);
                        SQLiteDatabaseHandler db = MainActivity.getDatabase();
                        if (db.tireExists(tire.getAddress())) {
                            db.updateTire(tire);
                            logthis("TRASMITTER UPDATED!");
                        } else {
                            db.addTire(tire);
                            logthis("TRASMITTER ADDED!");
                        }

                        stop_discover();
                    }
                }
            }

        }

        @Override
        public void onScanFailed(int errorCode) {
            logthis("Discovery onScanFailed: " + errorCode);
            discovering = false;
            discover.setText(R.string.startDiscovering);
            super.onScanFailed(errorCode);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_discover, container, false);
        logger = myView.findViewById(R.id.loggerd);
        discover = myView.findViewById(R.id.discover);
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discovering) {
                    stop_discover();
                } else {
                    start_discover();
                }
            }
        });

        myView.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().setVisibility(View.GONE);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("tag")
                        .replace(android.R.id.content, new HelpFragment(), "tag")
                        .commit();
            }
        });
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        return myView;
    }

    void stop_discover() {
        mBluetoothLeScanner.stopScan(mScanCallback);
        discovering = false;
        discover.setText(R.string.startDiscover);
    }

    void start_discover() {
        mBluetoothLeScanner.startScan(mScanCallback);
        discovering = true;
        discover.setText(R.string.stopDiscover);
    }

    public void logthis(String msg) {
        logger.append(msg + "\n");
        Log.d(TAG, msg);
    }
}
