package com.nopri.penjualanmainan.server;

public class BaseURL {
    public static String baseURL = "http://192.168.43.22:2019/";
    //guest
    public static String login       = baseURL + "user/login";

    public static String register    = baseURL + "user/registrasi";


    //Barang
    public static String databarang    = baseURL + "barang/databarang";

    public static String databarangAction    = baseURL + "barang/databarang/Action Figure";

    public static String databarangBoneka    = baseURL + "barang/databarang/Boneka";

    public static String databarangKoleksi    = baseURL + "barang/databarang/Barang Koleksi";

    public static String inputdatabarang    = baseURL + "barang/inputbarang";

    public static String Editdatabarang    = baseURL + "barang/ubah/";

    public static String hapusdatabarang    = baseURL + "barang/hapusbarang/";


    //Order
    public static String order = baseURL +"order/input";

    public static String getData = baseURL + "order/getAllOrder";

    public static String getdataTransaksi = baseURL + "order/dataOrderbyuser/";

    public static String EditTransaksi = baseURL + "order/edit/";
}
