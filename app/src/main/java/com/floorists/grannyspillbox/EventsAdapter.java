package com.floorists.grannyspillbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.floorists.grannyspillbox.classes.ScheduledEvent;
import com.floorists.grannyspillbox.utilities.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventsAdapter extends ArrayAdapter<ScheduledEvent> {

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public EventsAdapter(Context context, ArrayList<ScheduledEvent> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ScheduledEvent event = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scheduled_meds, parent, false);
        }

        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        TextView qtyTextView = convertView.findViewById(R.id.qtyTextView);
        TextView pillNameTextView = convertView.findViewById(R.id.pillNameTextView);

        timeTextView.setText(timeFormat.format(event.getTime()));
        qtyTextView.setText(Double.toString(event.getQty()));

        if(event.getMedication().getName() == null) {
            pillNameTextView.setText("Advil");
        } else {
            pillNameTextView.setText(event.getMedication().getName());

        }

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Remove medication from schedule?");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(event);
                        notifyDataSetChanged();
                        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                        sqLiteHelper.deleteEvent(event);
                    }
                }).create().show();
                return false;
            }
        });

        return convertView;
    }

}
