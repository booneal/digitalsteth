package com.boone.mddriven;

import java.math.BigInteger;
import java.util.UUID;

public class SampleGattAttributes {

    private static final UUID SERVICE_ID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private static final byte[] HM10_ADAPTER_ADDRESS = parseMAC("FC:45:C3:23:F7:87");

    public static byte[] getMAC(){
        return HM10_ADAPTER_ADDRESS;
    }

    public static UUID getServiceUUID(){
        return SERVICE_ID;
    }

    public static UUID getCharacteristicUUID(){
        return CHARACTERISTIC_UUID;
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }


    public static byte[] parseMAC(String macAddress) {
        String[] bytes = macAddress.split(":");
        byte[] parsed = new byte[bytes.length];

        for (int x = 0; x < bytes.length; x++) {
            BigInteger temp = new BigInteger(bytes[x], 16);
            byte[] raw = temp.toByteArray();
            parsed[x] = raw[raw.length - 1];
        }
        return parsed;
    }
}
