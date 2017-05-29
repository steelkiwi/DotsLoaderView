package steelkiwi.com.pointloaderview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import steelkiwi.com.library.DotsLoaderView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final DotsLoaderView view1 = (DotsLoaderView) findViewById(R.id.dotsLoaderView);
        Button show = (Button) findViewById(R.id.show);
        Button hide = (Button) findViewById(R.id.hide);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.show();
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.hide();
            }
        });
    }
}
