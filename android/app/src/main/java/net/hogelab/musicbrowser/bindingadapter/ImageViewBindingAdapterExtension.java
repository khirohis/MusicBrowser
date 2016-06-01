package net.hogelab.musicbrowser.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by kobayasi on 2016/04/08.
 */
public class ImageViewBindingAdapterExtension {

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int res){
        view.setImageResource(res);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Context context = imageView.getContext();
            if (context != null) {
                Picasso.with(context).load(imageUrl).into(imageView);
            }
        }
    }

    @BindingAdapter({"blurImageUrl"})
    public static void loadBlurImage(final ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Context context = imageView.getContext();
            if (context != null) {
                Picasso.with(context).load(imageUrl).into(imageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        Context context = imageView.getContext();
                        if (context != null) {
                            Blurry.with(context)
                                    .radius(10)
                                    .sampling(8)
                                    .async()
                                    .capture(imageView)
                                    .into(imageView);
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        }
    }
}
