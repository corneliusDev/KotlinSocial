package com.photoglyde.justincornelius.photoglyde.RecordVideo

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.photoglyde.justincornelius.photoglyde.Data.REQUEST_VIDEO_PERMISSIONS
import com.photoglyde.justincornelius.photoglyde.Data.VIDEO_PERMISSIONS


import com.photoglyde.justincornelius.photoglyde.R

/**
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
class ConfirmationDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?) =
            AlertDialog.Builder(activity)
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        parentFragment?.requestPermissions(VIDEO_PERMISSIONS,
                                REQUEST_VIDEO_PERMISSIONS)
                    }
                    .setNegativeButton(android.R.string.cancel) { _,_ ->
                        parentFragment?.activity?.finish()
                    }
                    .create()}

