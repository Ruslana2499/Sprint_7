package common.constants;

import common.entities.Courier;
import common.entities.Order;

public class Constants {
    public static final Courier COURIER = new Courier("kuslo", "pes2,5", "Stewie");
    public static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2M2U5MDliZjViMDU4ODAwM2RlNjk2NTciLCJpYXQiOjE2NzYyMTY3NjcsImV4cCI6MTY3NjgyMTU2N30.5Br4D0D5HuCKX1pOGXKlLRNNiP5vkaczMGHHiEALO_g";
    public static final String RESOURCE_URL ="http://qa-scooter.praktikum-services.ru";
    public static final Order ORDER = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha");
}