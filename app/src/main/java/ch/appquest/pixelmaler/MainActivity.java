package ch.appquest.pixelmaler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DrawingView drawingView;
	private ImageButton currentBrush;

	public void eraseClicked(View view) {
		if (view != currentBrush) {
			ImageButton imgView = (ImageButton) view;
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
			currentBrush.setImageDrawable(null);
			currentBrush = (ImageButton) view;
		}

		drawingView.setErase(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawingView = (DrawingView) findViewById(R.id.drawing);

		currentBrush = (ImageButton) findViewById(R.id.defaultColor);
		currentBrush.setImageDrawable(getResources().getDrawable(R.drawable.selected));
		String color = currentBrush.getTag().toString();
		drawingView.setColor(color);

        for(int i = 0; i < 7; i++){
            Button button = new Button(this);
            button.setHeight(30);
            button.setWidth(30);

            DrawingView squareLayout = (DrawingView)this.findViewById(R.id.drawing);
        }
	}

	private void onCreateNewDrawingAction() {
		AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
		newDialog.setTitle("New Drawing");
		newDialog.setMessage("Start a new drawing?");
		newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				drawingView.startNew();
				dialog.dismiss();
			}
		});
		newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		newDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add("New");
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				onCreateNewDrawingAction();
				return true;
			}
		});

		menuItem = menu.add("Log");
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onLogAction();
				return false;
			}
		});

		return true;
	}

	public void paintClicked(View view) {
		if (view != currentBrush) {
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawingView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
			currentBrush.setImageDrawable(null);
			currentBrush = (ImageButton) view;
		}
		drawingView.setErase(false);
	}

	private void onLogAction() {
		String messageToLog = PaintMapToJsonConverter.ParseMapToJson(this.drawingView.getPaintMap());

        Intent intent = new Intent("ch.appquest.intent.LOG");

        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "Logbook App not Installed", Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra("ch.appquest.taskname", "Pixel Maler");
        intent.putExtra("ch.appquest.logmessage", messageToLog);

        startActivity(intent);
	}

}
