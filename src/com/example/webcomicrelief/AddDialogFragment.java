package com.example.webcomicrelief;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AddDialogFragment extends DialogFragment {
	
	public interface DeleteDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	DeleteDialogListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (DeleteDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString());
		}
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.add_dialog_message)
				.setPositiveButton(R.string.add_dialog_manual, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						listener.onDialogPositiveClick(AddDialogFragment.this);
					}
				})
				.setNegativeButton(R.string.add_dialog_list, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						listener.onDialogNegativeClick(AddDialogFragment.this);
					}
				});
		return builder.create();
	}
	
	
}
