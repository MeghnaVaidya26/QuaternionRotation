package com.example.quaternationapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var mMyView: MyQuaternionView? = null //a custom view for drawing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMyView = MyQuaternionView(this);
        setContentView(mMyView);
      }
}