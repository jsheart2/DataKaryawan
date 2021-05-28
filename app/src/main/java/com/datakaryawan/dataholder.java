package com.datakaryawan;

public class dataholder {
    private String namapemohon;
    private String nickkaryawan;
    private String tanggallahir;
    private String gajikaryawan;

    dataholder(){}

    public dataholder(String namapemohon, String nickkaryawan, String tanggallahir, String gajikaryawan) {
        this.namapemohon = namapemohon;
        this.nickkaryawan = nickkaryawan;
        this.tanggallahir = tanggallahir;
        this.gajikaryawan = gajikaryawan;
    }

    public String getNamapemohon() {
        return namapemohon;
    }

    public void setNamapemohon(String namapemohon) {
        this.namapemohon = namapemohon;
    }

    public String getNickkaryawan() {
        return nickkaryawan;
    }

    public void setNickkaryawan(String nickkaryawan) {
        this.nickkaryawan = nickkaryawan;
    }

    public String getTanggallahir() {
        return tanggallahir;
    }

    public void setTanggallahir(String tanggallahir) {
        this.tanggallahir = tanggallahir;
    }

    public String getGajikaryawan() {
        return gajikaryawan;
    }

    public void setGajikaryawan(String gajikaryawan) {
        this.gajikaryawan = gajikaryawan;
    }
}