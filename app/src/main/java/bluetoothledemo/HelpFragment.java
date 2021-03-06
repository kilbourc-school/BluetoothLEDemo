package bluetoothledemo;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 2;
    //bluetooth device and code to turn the device on if needed.
    BluetoothAdapter mBluetoothAdapter = null;
    View myView = null;
    boolean tempPref = false;
    boolean presPref = false;
    private final BluetoothLeScanner mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_help, container, false);
        checkpermissions();
        startbt();
        showTireData();
        setupButtonClicks();
        mBluetoothLeScanner.startScan(mScanCallback);
        return myView;
    }

    private void setupButtonClicks() {
        Button button = myView.findViewById(R.id.btn_pressure);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                presPref = !presPref;
                showTireData();
            }
        });
        button = myView.findViewById(R.id.btn_temp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tempPref = !tempPref;
                showTireData();
            }
        });
        button = myView.findViewById(R.id.btn_discover);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getView().setVisibility(View.GONE);
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("tag")
                        .replace(android.R.id.content,new DiscoverFragment(), "tag")
                        .commit();
            }
            });
    }

    void showTireData() {
        SQLiteDatabaseHandler db = MainActivity.getDatabase();
        List<Tire> allTires = db.allTires();
        for (int i = 0; i < allTires.size(); i++) {
            Tire tire = allTires.get(i);
            TextView textView = null;
            if (i == 0) {
                textView = myView.findViewById(R.id.tire1);
            } else if (i == 1) {
                textView = myView.findViewById(R.id.tire2);
            } else if (i == 2) {
                textView = myView.findViewById(R.id.tire3);
            } else if (i == 3) {
                textView = myView.findViewById(R.id.tire4);
            }
            if (textView != null) {
                int pressure = tire.getCurPres();
                String pressureSign = " kPa";
                int temp = tire.getCurTemp();
                String tempSign = " °C";
                if (presPref) {
                    pressure = (int) (pressure / 6.895);
                    pressureSign = " PSI";
                }
                if (tempPref) {
                    temp = (int) ((temp * 1.8) + 32);
                    tempSign = " °F";
                }
                textView.setText("Tire " + (i+1) + "\n" + pressure + pressureSign + "\n" + temp + tempSign + "\n\n");
            }
        }
    }

    void checkpermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.REQUEST_ACCESS_COURSE_LOCATION);
        }
    }

    //This code will check to see if there is a bluetooth device and
    //turn it on if is it turned off.
    public void startbt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            System.out.println("This device does not support bluetooth");
            return;
        }
        //make sure bluetooth is enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result == null || result.getDevice() == null) {
            } else {
                if (result.getDevice().getName() != null) {
                    if (result.getDevice().getName().contains("TM")) {
                        byte[] dataBlock = result.getScanRecord().getBytes();
                        int temp = (dataBlock[23] & 0xFF) - 43;
                        int pressure = (((dataBlock[24] & 0xFF) & 0x70) << 4) | (dataBlock[22] & 0xFF);
                        SQLiteDatabaseHandler db = MainActivity.getDatabase();
                        if(db.tireExists(result.getDevice().getAddress())) {
                            db.updateTireRunning(result.getDevice().getAddress(),temp,pressure);
                            showTireData();
                            System.out.println("UPDATED");
                        }
                    }
                }
            }
        }
    };
}
