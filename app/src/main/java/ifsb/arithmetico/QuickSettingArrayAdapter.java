package ifsb.arithmetico;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sufian on 22/9/15.
 */
public class QuickSettingArrayAdapter extends ArrayAdapter<String> {
    static class QuickSettingHolder {
        public TextView tv_description;
        public RadioButton rb_label;
    }
    private Context context;
    private int layoutResourceId;
    private ArrayList<String> data;
    private RadioButton prevRBtn = null;
    private String selected = null;

    public QuickSettingArrayAdapter(Context context, int layoutResourceId, ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(View view, int position) {
        if (view instanceof Checkable)
            ((Checkable) view).setChecked(true);
        else if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                if (v instanceof Checkable)
                    ((Checkable) v).setChecked(true);
            }
        }
        selected = data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        QuickSettingHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new QuickSettingHolder();
            holder.rb_label = (RadioButton) row.findViewById(R.id.rb_label);
            holder.rb_label.setFocusable(false);
            holder.rb_label.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (prevRBtn != null)
                        prevRBtn.setChecked(false);
                    else
                        ((QuickSettingActivity) context).enableDone();

                    if (b) {
                        prevRBtn = (RadioButton) compoundButton;
                        selected = compoundButton.getText().toString();
                    }
                }

            });
//            holder.tv_label = (TextView) row.findViewById(R.id.tv_label);
                holder.tv_description=(TextView)row.findViewById(R.id.tv_description);
                holder.tv_description.setFocusable(false);
                row.setTag(holder);
            }
            else
            holder = (QuickSettingHolder) row.getTag();

        String quickSettingName = data.get(position);
        holder.rb_label.setText(quickSettingName);
        holder.tv_description.setText(QuickSetting.getDescription(quickSettingName));
        return row;
    }
}