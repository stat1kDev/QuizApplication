package stat1kDev.quizapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.utilities.ActivityUtilities;

public class AboutDevActivity extends BaseActivity {

    private ImageView imageView;
    private TextView tvDevTitle, tvDevSubTitle, tvDevText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);

        imageView = (ImageView) findViewById(R.id.imgDev);
        tvDevTitle = (TextView) findViewById(R.id.tvDevTitle);
        tvDevSubTitle = (TextView) findViewById(R.id.tvDevSubtitle);
        tvDevText = (TextView) findViewById(R.id.tvDevText);

        ininToolbar(true);
        setToolbarTitle(getString(R.string.about_dev));
        enableUpButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickView(View view) {
        ActivityUtilities.getInstance().invokeCustomUrlActivity(AboutDevActivity.this,  CustomUrlActivity.class,
                getResources().getString(R.string.site), getResources().getString(R.string.site_url), false);
    }
}
