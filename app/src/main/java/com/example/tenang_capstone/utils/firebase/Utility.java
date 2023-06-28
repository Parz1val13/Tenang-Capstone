package com.example.tenang_capstone.utils.firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public String currentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
}
