package com.wanjian.sak.view;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.wanjian.sak.R;
import com.wanjian.sak.config.Config;
import com.wanjian.sak.config.Item;
import com.wanjian.sak.converter.ISizeConverter;
import com.wanjian.sak.page.core.Pager;

import java.util.List;

public class OptPanelView extends LinearLayout {
    private GridView function;
    private NumberPicker startRange;
    private NumberPicker endRange;
    private LinearLayout unitGroup;
    private CheckBox clipDraw;
    private OptPanelViewHolder mHolder;
    private OnClickListener confirmListener;
    private OnPagerActionListener mPAListener;

    public void setConfirmListener(OnClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public void setPagerActionListener(OnPagerActionListener actionListener) {
        this.mPAListener = actionListener;
    }

    public OptPanelView(@NonNull Context context) {
        this(context, null);
    }

    public OptPanelView(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.sak_opt_panel_view, this);

        function = findViewById(R.id.function);
        startRange = findViewById(R.id.startRange);
        endRange = findViewById(R.id.endRange);
        unitGroup = findViewById(R.id.unitGroup);
        clipDraw = findViewById(R.id.clipDraw);

        findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPAListener != null) {
                    mPAListener.onPageFinish();
                }
                if (confirmListener != null) {
                    confirmListener.onClick(v);
                }
            }
        });
        findViewById(R.id.help).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = Uri.parse("https://github.com/android-notes/SwissArmyKnife/blob/master/README.md");
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    getContext().startActivity(intent);
                    if (mPAListener != null) {
                        mPAListener.onPageFinish();
                    }
                    if (confirmListener != null) {
                        confirmListener.onClick(v);
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public void attach(Application application, Config config) {
        mHolder = new OptPanelViewHolder(application, config);
        setFunctions();
        setSizeConverter();
        setRange();
        setClipDraw();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void setRange() {
        final Config config = mHolder.getConfig();
        config.setStartRange(0);
        config.setEndRange(config.getMaxRange() / 2);
        startRange.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        startRange.setMinValue(config.getMinRange());
        startRange.setMaxValue(config.getMaxRange());
        startRange.setValue(config.getStartRange());
        startRange.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setStartRange(newVal);
                mHolder.changeStartRange(newVal);
            }
        });

        endRange.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        endRange.setMinValue(config.getMinRange());
        endRange.setMaxValue(config.getMaxRange());
        endRange.setValue(config.getEndRange());
        endRange.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                config.setEndRange(newVal);
                mHolder.changeEndRange(newVal);
            }
        });

    }

    private void setClipDraw() {
        final Config config = mHolder.getConfig();
        clipDraw.setChecked(config.isClipDraw());
        clipDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setClipDraw(isChecked);
                mHolder.changeClip(isChecked);
            }
        });
    }

    private void setSizeConverter() {
        final Config config = mHolder.getConfig();
        List<ISizeConverter> sizeConverters = config.getSizeConverters();
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final ISizeConverter converter : sizeConverters) {
            //fuck 4.1
            final CheckBox button = (CheckBox) inflater.inflate(R.layout.sak_radiobutton, unitGroup, false);
            unitGroup.addView(button);
            button.setText(converter.desc());
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = unitGroup.getChildCount() - 1; i > -1; i--) {
                        ((CheckBox) unitGroup.getChildAt(i)).setChecked(false);
                    }
                    button.setChecked(true);
                    ISizeConverter.CONVERTER = converter;
                    mHolder.changeSizeConvert(converter);
                }
            });
        }
        unitGroup.getChildAt(0).performClick();
    }


    private void setFunctions() {
        final Config config = mHolder.getConfig();
        setAdapter(config.getFunctionList());
        function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = config.getFunctionList().get(position);
                if (Item.TYPE.LAYER == item.getType()) {
                    item.setEnable(!item.isEnable());
                    //GridScroller 不复用控件，可以直接修改view
                    Holder holder = (Holder) view.getTag(R.layout.sak_function_item);
                    if (item.isEnable()) {
                        holder.check.setVisibility(VISIBLE);
                    } else {
                        holder.check.setVisibility(GONE);
                    }

                    mHolder.update();
                } else {
                    if (null != mPAListener) {
                        mPAListener.onPagerAction(item.getPagerClass());
                    }
                }
            }
        });
    }

    private void setAdapter(final List<Item> iLayers) {

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        function.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return iLayers.size();
            }

            @Override
            public Item getItem(int position) {
                return iLayers.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.sak_function_item, function, false);
                    Holder holder = new Holder();
                    holder.icon = convertView.findViewById(R.id.icon);
                    holder.title = convertView.findViewById(R.id.title);
                    holder.check = convertView.findViewById(R.id.check);
                    convertView.setTag(R.layout.sak_function_item, holder);
                }
                Holder holder = (Holder) convertView.getTag(R.layout.sak_function_item);
                Item layer = getItem(position);
                holder.title.setText(layer.name);
                holder.icon.setImageDrawable(layer.icon);
                if (layer.isEnable()) {
                    holder.check.setVisibility(VISIBLE);
                } else {
                    holder.check.setVisibility(GONE);
                }
                return convertView;
            }
        });

    }

    public void destroy() {
        if (null != mHolder) {
            mHolder.destroy();
        }
    }

    static class Holder {
        ImageView icon;
        TextView title;
        View check;
    }

    public interface OnPagerActionListener {
        void onPagerAction(Class<? extends Pager> type);

        void onPageFinish();
    }
}
