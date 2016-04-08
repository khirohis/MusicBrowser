package net.hogelab.musicbrowser.mvvm.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

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
//                Glide.with(context).load(imageUrl).animate(android.R.anim.fade_in).into(imageView);
            }
        }
    }
}
