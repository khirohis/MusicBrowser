package net.hogelab.musicbrowser.bindingadapter;

import android.content.ContentUris;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.hogelab.musicbrowser.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by kobayasi on 2016/04/08.
 */
public class ImageViewBindingAdapterExtension {

    private static final String TAG = ImageViewBindingAdapterExtension.class.getSimpleName();

    private static final Uri ALBUMART_URI = Uri.parse("content://media/external/audio/albumart");


    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int res){
        view.setImageResource(res);
    }


    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {
            Context context = imageView.getContext();
            if (context != null) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.media_music)
                        .error(R.drawable.media_music);

                Glide.with(imageView)
                        .setDefaultRequestOptions(requestOptions)
                        .load(imageUrl)
                        .apply(RequestOptions.centerCropTransform())
                        .into(imageView);
            }
        }
    }


    @BindingAdapter({"imageAlbumId"})
    public static void loadImageAlbumId(ImageView imageView, String imageAlbumId) {
        if (imageAlbumId != null) {
            Context context = imageView.getContext();
            if (context != null) {
                try {
                    Uri uri = ContentUris.withAppendedId(ALBUMART_URI, Long.parseLong(imageAlbumId));
                    ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        FileDescriptor fd = pfd.getFileDescriptor();
                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            Log.d(TAG, "loadImageAlbumId: loaded albumId=" + imageAlbumId);
                        }
                    }
                } catch (FileNotFoundException e) {
                }
            }
        }
    }
}
