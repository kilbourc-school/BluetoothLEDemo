package bluetoothledemo;

public class Tire {
    private String address;
    private int curTemp;
    private int baseTemp;
    private int tempPref;//0 is C, 1 is F
    private int curPres;
    private int basePres;
    private int presPref;//0 is kPa, 1 is PSI

    public Tire(String address, int curTemp, int baseTemp, int tempPref, int curPres, int basePres, int presPref) {
        this.address = address;
        this.curTemp = curTemp;
        this.baseTemp = baseTemp;
        this.tempPref = tempPref;
        this.curPres = curPres;
        this.basePres = basePres;
        this.presPref = presPref;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "address='" + address + '\'' +
                ", curTemp=" + curTemp +
                ", baseTemp=" + baseTemp +
                ", tempPref=" + tempPref +
                ", curPres=" + curPres +
                ", basePres=" + basePres +
                ", presPref=" + presPref +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(int curTemp) {
        this.curTemp = curTemp;
    }

    public int getBaseTemp() {
        return baseTemp;
    }

    public void setBaseTemp(int baseTemp) {
        this.baseTemp = baseTemp;
    }

    public int getTempPref() {
        return tempPref;
    }

    public void setTempPref(int tempPref) {
        this.tempPref = tempPref;
    }

    public int getCurPres() {
        return curPres;
    }

    public void setCurPres(int curPres) {
        this.curPres = curPres;
    }

    public int getBasePres() {
        return basePres;
    }

    public void setBasePres(int basePres) {
        this.basePres = basePres;
    }

    public int getPresPref() {
        return presPref;
    }

    public void setPresPref(int presPref) {
        this.presPref = presPref;
    }
}
