package com.example.myapplication;

import android.hardware.ConsumerIrManager;

public class NecCommand_32Bit {
    private ConsumerIrManager manager;
    private final int frequency = 38028;
    private int code;
    private final int[] pattern;

    public NecCommand_32Bit() {
        pattern = new int[68];
    }

    public void setManager(ConsumerIrManager manager) {
        this.manager = manager;
    }

    public void code(int code){
        this.code = code;
        setPattern();
    }

    public void transmit(){
        manager.transmit(frequency, pattern);
    }

    private void setPattern(){
        //.241246
        //first and last burst pairs are conventionally set depending on the manufacturer, like the repeat code
        //first burst pair, the lead in burst
        pattern[0] = 8967;
        pattern[1] = 4470;
        //last burst pair, the lead out burst
        pattern[66] = 552;
        pattern[67] = 39891;

        int p1 = 552, p2 = 1682;
        int[] bits = new int[32];
        for(int i = 31; i >= 0; i--){
            bits[i] = code%2;
            code/=2;
        }

        for(int i = 0; i < 32; i++){
            if(bits[i] == 0){
                pattern[2*i+2] = pattern[2*i+3] = p1;
            }
            else{
                pattern[2*i+2] = p1; pattern[2*i+3] = p2;
            }
        }
    }

    public boolean hasIrEmitter(){
        return manager.hasIrEmitter();
    }

}
