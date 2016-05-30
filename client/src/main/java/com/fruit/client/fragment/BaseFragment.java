package com.fruit.client.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.fruit.core.api.VolleyResponse;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

/**
 * Created by user on 2016/2/26.
 */
public abstract class BaseFragment extends Fragment implements VolleyResponse{
    private SimpleProgressDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResponse(Object response, int taskid) {

    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {

    }

    public void showLoadingDialog(String msg)
    {
        if (mDialog==null)
        {
            mDialog = SimpleProgressDialog.createLoadingDialog(getActivity());
        }
        if (!mDialog.isShowing())
        {
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDialog.dismiss();
                }
            });
            mDialog.setMsg(msg);
            mDialog.show();
        }
    }

    public void hideLoadingDialog()
    {
        if (mDialog!=null && mDialog.isShowing())
        {
            mDialog.dismiss();
        }
    }
}
