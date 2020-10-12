package com.example.ea2soa.data;

import android.os.AsyncTask;

import com.android.volley.toolbox.Volley;
import com.example.ea2soa.data.model.User;

public class RegisterRequest extends AsyncTask<User,Void,Boolean> {

    @Override
    protected Boolean doInBackground(User... user) {
        Volley.newRequestQueue(null);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
//        showDialog("Downloaded " + result + " bytes");
    }
}
