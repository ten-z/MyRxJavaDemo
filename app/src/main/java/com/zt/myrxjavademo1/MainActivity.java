package com.zt.myrxjavademo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zt.myrxjavademo1.myaspectj.MyAspectTest;
import com.zt.myrxjavademo1.myoperator.MyOperatorActivity;

/**
 * 本事例建议结合我的博客看，主要理论在博客中有详细介绍
 * 通过两种方法实现"上行"事件触发流中插入自己的代码，本例主要以输出当前线程名为例，可以自定义成别的内容:
 * {@link MyOperatorActivity}:通过自定义操作符实现
 * {@link MyAspectTest}:通过AOP的方式实现，这里使用的是Aspectj注入代码
 *
 * @author ten-z
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.bt_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOperatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
