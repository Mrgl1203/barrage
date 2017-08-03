package barrage.gl.com.barrage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private XCDanmuView mDanmuView;
    private EditText editText;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mDanmuView.createDanmuView("66666", false);
                    handler.sendEmptyMessageDelayed(0, 400);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDanmuView();
        initListener();
        initEdit();

    }

    private void initEdit() {
        editText = (EditText) findViewById(R.id.edit);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    mDanmuView.createDanmuView(content, true);
                    editText.setText("");
                }
                return true;
            }
        });
    }

    private void initListener() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDanmuView.isWorking()) {
                    mDanmuView.hide();
                    ((Button) view).setText("开启弹幕");
                } else {
                    mDanmuView.start();
                    ((Button) view).setText("关闭弹幕");
                }
            }
        });

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDanmuView.changeDirection();
            }
        });
    }

    private void initDanmuView() {
        mDanmuView = (XCDanmuView) findViewById(R.id.danmu);
        mDanmuView.start();
        handler.sendEmptyMessageDelayed(0, 500);

    }

}
