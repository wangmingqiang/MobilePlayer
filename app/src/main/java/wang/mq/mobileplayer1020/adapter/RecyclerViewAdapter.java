package wang.mq.mobileplayer1020.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import wang.mq.mobileplayer1020.R;
import wang.mq.mobileplayer1020.activity.ShowImageAndGifActivity;
import wang.mq.mobileplayer1020.bean.NetAudioBean;
import wang.mq.mobileplayer1020.utils.Utils;

/**
 * Created by wangmingqiang on 2017/1/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private final Context mContent;
    private final List<NetAudioBean.ListBean> listDatas;



    /**
     * 视频
     */
    private static final int TYPE_VIDEO = 0;

    /**
     * 图片
     */
    private static final int TYPE_IMAGE = 1;

    /**
     * 文字
     */
    private static final int TYPE_TEXT = 2;

    /**
     * GIF图片
     */
    private static final int TYPE_GIF = 3;


    /**
     * 软件推广
     */
    private static final int TYPE_AD = 4;



    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public int getItemViewType(int position) {

        String type=listDatas.get(position).getType();
        int itemViewType = TYPE_VIDEO;
        //根据位置，从列表中得到一个数据对象

        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;//广播
        }
        return itemViewType;
    }

    public RecyclerViewAdapter(Context mContext, List<NetAudioBean.ListBean> listDatas) {
        this.mContent=mContext;
        this.listDatas=listDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View convertView;

        switch (viewType) {
            case TYPE_VIDEO://视频

                viewHolder = new VideoHoder(View.inflate(mContent, R.layout.all_video_item, null));
                break;
            case TYPE_IMAGE://图片

                viewHolder = new ImageHolder(View.inflate(mContent, R.layout.all_image_item, null));
                break;
            case TYPE_TEXT://文字

                viewHolder = new TextHolder(View.inflate(mContent, R.layout.all_text_item, null));
                break;
            case TYPE_GIF://gif

                viewHolder = new gifHolder(View.inflate(mContent, R.layout.all_gif_item, null));
                break;
            case TYPE_AD://软件广告

                viewHolder = new ADfHolder(View.inflate(mContent, R.layout.all_ad_item, null));
                break;
            default:
                viewHolder = new ADfHolder(View.inflate(mContent, R.layout.all_ad_item, null));
                break;
        }


        return viewHolder;
    }





    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType=getItemViewType(position);
        if(itemViewType==TYPE_VIDEO) {
            VideoHoder videoHoder= (VideoHoder) holder;
            videoHoder.setData(listDatas.get(position));
        }else if (itemViewType == TYPE_IMAGE) {
            ImageHolder imageHolder = (ImageHolder) holder;
            imageHolder.setData(listDatas.get(position));
        } else if (itemViewType == TYPE_TEXT) {
            TextHolder textHolder = (TextHolder) holder;
            textHolder.setData(listDatas.get(position));
        } else if (itemViewType == TYPE_GIF) {
            gifHolder gifHolder = (gifHolder) holder;
            gifHolder.setData(listDatas.get(position));
        } else {
            ADfHolder adHolder = (ADfHolder) holder;
//            adHolder.setData(listDatas.get(position));
        }
    }



    class BaseViewHolder extends RecyclerView.ViewHolder{

        ImageView ivHeadpic;
        TextView tvName;
        TextView tvTimeRefresh;
        ImageView ivRightMore;
        ImageView ivVideoKind;
        TextView tvVideoKindText;
        TextView tvShenheDingNumber;
        TextView tvShenheCaiNumber;
        TextView tvPostsNumber;
        LinearLayout llDownload;

        public BaseViewHolder(View convertView) {
            super(convertView);
            //公共的
            ivHeadpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvTimeRefresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
            ivRightMore = (ImageView) convertView.findViewById(R.id.iv_right_more);
            //bottom
            ivVideoKind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
            tvVideoKindText = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
            tvShenheDingNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_ding_number);
            tvShenheCaiNumber = (TextView) convertView.findViewById(R.id.tv_shenhe_cai_number);
            tvPostsNumber = (TextView) convertView.findViewById(R.id.tv_posts_number);
            llDownload = (LinearLayout) convertView.findViewById(R.id.ll_download);

            //设置item的点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetAudioBean.ListBean listEntity = listDatas.get(getLayoutPosition());
                    if(listEntity !=null ){
                        //3.传递视频列表
                        Intent intent = new Intent(mContent,ShowImageAndGifActivity.class);
                        if(listEntity.getType().equals("gif")){
                            String url = listEntity.getGif().getImages().get(0);
                            intent.putExtra("url",url);
                            mContent.startActivity(intent);
                        }else if(listEntity.getType().equals("image")){
                            String url = listEntity.getImage().getThumbnail_small().get(0);
                            intent.putExtra("url",url);
                            mContent.startActivity(intent);
                        }
                    }
                }
            });
        }
        /**
         * 设置公共的数据
         *
         * @param mediaItem
         */
        public void setData(NetAudioBean.ListBean mediaItem) {
            if (mediaItem.getU() != null && mediaItem.getU().getHeader() != null && mediaItem.getU().getHeader().get(0) != null) {
                x.image().bind(ivHeadpic, mediaItem.getU().getHeader().get(0));
            }
            if (mediaItem.getU() != null && mediaItem.getU().getName() != null) {
                tvName.setText(mediaItem.getU().getName() + "");
            }

            tvTimeRefresh.setText(mediaItem.getPasstime());

            //设置标签
            List<NetAudioBean.ListBean.TagsEntity> tagsEntities = mediaItem.getTags();
            if (tagsEntities != null && tagsEntities.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < tagsEntities.size(); i++) {
                    buffer.append(tagsEntities.get(i).getName() + " ");
                }
                tvVideoKindText.setText(buffer.toString());
            }

            //设置点赞，踩,转发

            tvShenheDingNumber.setText(mediaItem.getUp());
            tvShenheCaiNumber.setText(mediaItem.getDown() + "");
            tvPostsNumber.setText(mediaItem.getForward() + "");

        }
    }

    class VideoHoder extends BaseViewHolder{

        Utils utils;
        TextView tvContext;
        JCVideoPlayerStandard jcvVideoplayer;
        TextView tvPlayNums;
        TextView tvVideoDuration;
        ImageView ivCommant;
        TextView tvCommantContext;

        VideoHoder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            utils = new Utils();
            tvPlayNums = (TextView) convertView.findViewById(R.id.tv_play_nums);
            tvVideoDuration = (TextView) convertView.findViewById(R.id.tv_video_duration);
            ivCommant = (ImageView) convertView.findViewById(R.id.iv_commant);
            tvCommantContext = (TextView) convertView.findViewById(R.id.tv_commant_context);
            jcvVideoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
        }

        //绑定数据
        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);

            //设置文本-所有的都有,只有广告没有哦
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            //视频特有的------------------------
            //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
            boolean setUp = jcvVideoplayer.setUp(
                    mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            //加载图片
            if (setUp) {
//                ImageLoader.getInstance().displayImage(mediaItem.getVideo().getThumbnail().get(0),
//                        jcvVideoplayer.thumbImageView);
                Glide.with(mContent).load(mediaItem.getVideo().getThumbnail().get(0)).into(jcvVideoplayer.thumbImageView);
            }
            tvPlayNums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            tvVideoDuration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        }
    }
    class ImageHolder extends BaseViewHolder{

        TextView tvContext;
        ImageView ivImageIcon;

        ImageHolder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);

        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            //设置文本-所有的都有
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
            //图片特有的

            ivImageIcon.setImageResource(R.drawable.bg_item);
            if (mediaItem.getImage() != null && mediaItem.getImage() != null && mediaItem.getImage().getSmall() != null) {
                Glide.with(mContent).load(mediaItem.getImage().getDownload_url().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImageIcon);
            }


        }
    }
    class TextHolder extends BaseViewHolder{

        TextView tvContext;

        TextHolder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);


        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            //设置文本-所有的都有
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());
        }
    }
    class gifHolder extends BaseViewHolder{

        TextView tvContext;
        ImageView ivImageGif;
        private ImageOptions imageOptions;

        gifHolder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            ivImageGif = (ImageView) convertView.findViewById(R.id.iv_image_gif);

            imageOptions = new ImageOptions.Builder()
                    //包裹类型
                    .setSize(ViewGroup.LayoutParams.WRAP_CONTENT, -2)
                    //设置圆角
                    .setRadius(DensityUtil.dip2px(5))
                    .setIgnoreGif(false)//是否忽略gif图。false表示不忽略。不写这句，默认是true
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.drawable.video_default)
                    .setFailureDrawableId(R.drawable.video_default)
                    .build();

        }

        public void setData(NetAudioBean.ListBean mediaItem) {
            super.setData(mediaItem);
            //设置文本-所有的都有
            tvContext.setText(mediaItem.getText() + "_" + mediaItem.getType());

            //下面是gif
            if (mediaItem.getGif() != null && mediaItem.getGif() != null && mediaItem.getGif().getImages() != null) {
//                Glide.with(context).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivImageGif);
                x.image().bind(ivImageGif, mediaItem.getGif().getImages().get(0), imageOptions);
            }

        }
    }
    class ADfHolder extends RecyclerView.ViewHolder{

        TextView tvContext;
        ImageView ivImageIcon;
        Button btnInstall;

        ADfHolder(View convertView) {
            super(convertView);
            //中间公共部分 -所有的都有
            tvContext = (TextView) convertView.findViewById(R.id.tv_context);
            btnInstall = (Button) convertView.findViewById(R.id.btn_install);
            ivImageIcon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
        }
    }


}
