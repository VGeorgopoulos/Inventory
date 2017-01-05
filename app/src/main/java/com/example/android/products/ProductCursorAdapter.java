package com.example.android.products;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.products.data.ProductContract;

import static com.example.android.products.data.ProductContract.*;

/**
 * Created by neuromancer on 5/1/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        //Find individual views that we want to modify in the list from layout
        ImageView productImageView = (ImageView) view.findViewById(R.id.product_image);
        TextView productNameView = (TextView) view.findViewById(R.id.product_name);
        TextView productQuantityView = (TextView) view.findViewById(R.id.product_quantity);
        TextView productPriceView = (TextView) view.findViewById(R.id.product_price);
        ImageButton newOrder = (ImageButton) view.findViewById(R.id.new_order);

        final int position = cursor.getPosition();

        newOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cursor.moveToPosition(position);

                //Uri of current product
                int productIdColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
                final long productId = cursor.getLong(productIdColumnIndex);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);

                //Find the column of the quantity
                int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);

                //Quantity of current product
                String productQuantity = cursor.getString(quantityColumnIndex);

                int newQuantity = Integer.parseInt(productQuantity);

                //As long as quantity is greater that 0 quantity--
                if (newQuantity > 0) {
                    newQuantity--;

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

                    int updateRow = context.getContentResolver().update(currentProductUri, contentValues, null, null);
                } else {
                    Toast.makeText(context, "Quantity is already at 0.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //Find the columns of product attributes that we're interested in
        int imageColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

        String productImage = cursor.getString(imageColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);

        //Update the TextViews and ImageViews with the attributes for the current product
        productImageView.setImageURI(Uri.parse(productImage));
        productNameView.setText(productName);
        productQuantityView.setText(productQuantity);
        productPriceView.setText(productPrice);
    }
}
