package com.kitsune.commonutils;

import sun.util.resources.hr.CalendarData_hr;

public class Key {

    public String decrypt(String password) {
        char keyC[] = password.toCharArray();
        for(int i = 0 ; i < keyC.length ; i ++){
            keyC[i] = (char) (keyC[i] ^ 5000);
        }
        return new String(keyC);
    }

    public String simpleDecrypt(String passWord) {
        char keyC[] = passWord.toCharArray();
        for(int i = 0 ; i < keyC.length ; i ++  ) {
            if((keyC[i] == 'A') || (keyC[i] == 'a')){
                keyC[i] = (char)(keyC[i] + 25);
            }else if(keyC[i] == '0'){
                keyC[i] = (char)(keyC[i] + 9);
            }
            else {
                keyC[i] = (char) (keyC[i] - 1);
            }
        }

        return new String(keyC);
    }

}
