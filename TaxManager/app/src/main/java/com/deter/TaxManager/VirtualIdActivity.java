package com.deter.TaxManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Identity;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.utils.Log;

import java.util.ArrayList;
import java.util.List;

import static com.deter.TaxManager.APPConstans.TAG_BAIDU;
import static com.deter.TaxManager.APPConstans.TAG_CTRIP;
import static com.deter.TaxManager.APPConstans.TAG_DIANPING;
import static com.deter.TaxManager.APPConstans.TAG_DIDI;
import static com.deter.TaxManager.APPConstans.TAG_DOUBAN;
import static com.deter.TaxManager.APPConstans.TAG_IMEI;
import static com.deter.TaxManager.APPConstans.TAG_IMSI;
import static com.deter.TaxManager.APPConstans.TAG_JD;
import static com.deter.TaxManager.APPConstans.TAG_LUMI;
import static com.deter.TaxManager.APPConstans.TAG_MOMO;
import static com.deter.TaxManager.APPConstans.TAG_PHONE;
import static com.deter.TaxManager.APPConstans.TAG_QQ;
import static com.deter.TaxManager.APPConstans.TAG_QQMAIL;
import static com.deter.TaxManager.APPConstans.TAG_SINAWEIBO;
import static com.deter.TaxManager.APPConstans.TAG_SUNING;
import static com.deter.TaxManager.APPConstans.TAG_TAOBAO;
import static com.deter.TaxManager.APPConstans.TAG_WEIXIN;

public class VirtualIdActivity extends AppCompatActivity {

    private static final String TAG = "VirtualIdActivity";

    private final ArrayMap<String, Integer> TAGS_TYPE_MESSAGING = new ArrayMap<String,
            Integer>() {
        {
            put(TAG_PHONE, R.drawable.phone);
            put(TAG_IMEI, R.drawable.imei);
            put(TAG_IMSI, R.drawable.imsi);
        }
    };

    private final ArrayMap<String, Integer> TAGS_TYPE_SOCIAL = new ArrayMap<String,
            Integer>() {
        {
            put(TAG_QQ, R.drawable.qq);
            put(TAG_WEIXIN, R.drawable.wechat);
            put(TAG_SINAWEIBO, R.drawable.weibo);
        }
    };

    private final ArrayMap<String, Integer> TAGS_TYPE_OTHERS = new ArrayMap<String,
            Integer>() {
        {
            put(TAG_TAOBAO, R.drawable.taobao);
            put(TAG_BAIDU, R.drawable.baidu);
            put(TAG_DOUBAN, R.drawable.douban);
            put(TAG_LUMI, R.drawable.lumi);
            put(TAG_MOMO, R.drawable.momo);
            put(TAG_CTRIP, R.drawable.ctrip);
            put(TAG_DIDI, R.drawable.didi);
            put(TAG_DIANPING, R.drawable.dianping);
            put(TAG_JD, R.drawable.jd);
            put(TAG_SUNING, R.drawable.suning);
            put(TAG_QQMAIL, R.drawable.qqmail);
        }
    };

    private List<VirtualID> messagings = new ArrayList<>();

    private List<VirtualID> socials = new ArrayList<>();

    private List<VirtualID> others = new ArrayList<>();

    private View messagingContainer;

    private View socialContainer;

    private View othersContainer;

    private VirtualIdAdapter virtualIdAdapter;

    private VirtualIdAdapter virtualIdAdapter1;

    private VirtualIdAdapter virtualIdAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_id);
        Intent intent = getIntent();
        String deviceMac = intent.getStringExtra("deviceMac");

        ImageView back = (ImageView) findViewById(R.id.back);
        TextView title = (TextView) findViewById(R.id.title);
        messagingContainer = findViewById(R.id.type_messaging);
        socialContainer = findViewById(R.id.type_social);
        othersContainer = findViewById(R.id.type_others);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.virtual_id_messaging);
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.virtual_id_social);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.virtual_id_others);


        title.setText(R.string.around_virtual_id);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        virtualIdAdapter = new VirtualIdAdapter(messagings, this);
        virtualIdAdapter1 = new VirtualIdAdapter(socials, this);
        virtualIdAdapter2 = new VirtualIdAdapter(others, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 3);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        recyclerView.setAdapter(virtualIdAdapter);
        recyclerView1.setAdapter(virtualIdAdapter1);
        recyclerView2.setAdapter(virtualIdAdapter2);

        recyclerView.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView1.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView2.addItemDecoration(new SpaceItemDecoration(30));

        List<Identity> resultList = DataManager.getInstance(this).queryIdentity(deviceMac);
        if (DtConstant.DEBUG_MODE) {
            getIdentityForTypes(mockData());
        } else {
            if (resultList != null) {
                Log.d(TAG, "reultlist size =" + resultList.size());
                getIdentityForTypes(resultList);
            }
        }

    }

    private List<Identity> mockData() {
        return new ArrayList<Identity>() {
            {
                Identity identity = new Identity();
                identity.setTag(TAG_QQ);
                identity.setValue("123455455");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_WEIXIN);
                identity.setValue("hahaha");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_MOMO);
                identity.setValue("momos");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_LUMI);
                identity.setTag("lumifs");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_BAIDU);
                identity.setValue("pt");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_PHONE);
                identity.setValue("13678567890");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_PHONE);
                identity.setValue("13876564534");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_IMSI);
                identity.setValue("34538769");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_IMEI);
                identity.setValue("535555656");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_IMEI);
                identity.setValue("535555656");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_JD);
                identity.setValue("sb");
                add(identity);

                identity = new Identity();
                identity.setTag(TAG_TAOBAO);
                identity.setValue("535555656");
                add(identity);

            }
        };
    }

    private void getIdentityForTypes(List<Identity> identities) {

        for (Identity identity : identities) {
            Log.d(TAG, "identity.getTag() =" + identity.getTag());
            if (TAGS_TYPE_MESSAGING.containsKey(identity.getTag())) {
                Log.d(TAG, "TAGS_TYPE_MESSAGING.get(identity.getTag())) 11=" +
                        TAGS_TYPE_MESSAGING.get(identity.getTag()));
                messagings.add(new VirtualID(identity.getTag(), identity.getValue(),
                        TAGS_TYPE_MESSAGING.get(identity.getTag())));
            } else if (TAGS_TYPE_SOCIAL.containsKey(identity.getTag())) {
                Log.d(TAG, "TAGS_TYPE_SOCIAL.get(identity.getTag())) 22=" + TAGS_TYPE_SOCIAL.get
                        (identity.getTag()));
                socials.add(new VirtualID(identity.getTag(), identity.getValue(),
                        TAGS_TYPE_SOCIAL.get(identity.getTag())));
            } else if (TAGS_TYPE_OTHERS.containsKey(identity.getTag())) {
                Log.d(TAG, "TAGS_TYPE_OTHERS.get(identity.getTag())) 33=" + TAGS_TYPE_OTHERS.get
                        (identity.getTag()));
                others.add(new VirtualID(identity.getTag(), identity.getValue(), TAGS_TYPE_OTHERS
                        .get(identity.getTag())));
            }

        }

        if (messagings.size() == 0) {
            messagingContainer.setVisibility(View.GONE);
        } else {
            virtualIdAdapter.setVirtualIdData(messagings);
        }

        if (socials.size() == 0) {
            socialContainer.setVisibility(View.GONE);
        } else {
            virtualIdAdapter1.setVirtualIdData(socials);
        }
        if (others.size() == 0) {
            othersContainer.setVisibility(View.GONE);
        } else {
            virtualIdAdapter2.setVirtualIdData(others);
        }
        if(messagings.size() == 0 && socials.size() == 0 && others.size() == 0){
            findViewById(R.id.no_data).setVisibility(View.VISIBLE);
        }
    }

    private static class VirtualID {

        private String tag;

        private String value;

        private int iconRes;

        public VirtualID(String tag, String value, int iconRes) {
            this.tag = tag;
            this.value = value;
            this.iconRes = iconRes;
        }

        public int getIconRes() {
            return iconRes;
        }

        public void setIconRes(int iconRes) {
            this.iconRes = iconRes;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        int mSpace;

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView
                .State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                outRect.left = 0;
            }

        }
    }

    private class VirtualIdAdapter extends RecyclerView.Adapter {

        Context context;

        List<VirtualID> identities;

        public VirtualIdAdapter(List<VirtualID> identities, Context context) {

            this.identities = identities;
            this.context = context;
        }

        public void setVirtualIdData(List<VirtualID> identities) {
            this.identities = identities;
            Log.d(TAG, "setVirtualIdData identities size =" + this.identities.size());
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_virtual_id_item,
                    parent, false);
            Log.d(TAG, "onCreateViewHolder");
            return new VirtualIdHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder");
            VirtualID identity = identities.get(position);
            if (holder instanceof VirtualIdHolder) {
                ((VirtualIdHolder) holder).virtualIdIcon.setImageResource(identity.getIconRes());
                ((VirtualIdHolder) holder).virtualIdInfo.setText(identity.getValue());
            }
        }

        @Override
        public int getItemCount() {
            return identities.size();
        }

    }

    private class VirtualIdHolder extends RecyclerView.ViewHolder {

        private ImageView virtualIdIcon;

        private TextView virtualIdInfo;

        public VirtualIdHolder(View itemView) {
            super(itemView);
            virtualIdIcon = (ImageView) itemView.findViewById(R.id.virual_id_icon);
            virtualIdInfo = (TextView) itemView.findViewById(R.id.virtual_id_info);
        }
    }

}
