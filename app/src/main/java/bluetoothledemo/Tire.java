package bluetoothledemo;

public class Tire {
    private String address;
    private int curTemp;
    private int baseTemp;
    private int curPres;
    private int basePres;

    public Tire(String address, int curTemp, int baseTemp, int curPres, int basePres) {
        this.address = address;
        this.curTemp = curTemp;
        this.baseTemp = baseTemp;
        this.curPres = curPres;
        this.basePres = basePres;
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


    @Override
    public String toString() {
        return "Tire{" +
                "address='" + address + '\'' +
                ", curTemp=" + curTemp +
                ", baseTemp=" + baseTemp +
                ", curPres=" + curPres +
                ", basePres=" + basePres +
                '}';
    }
}
