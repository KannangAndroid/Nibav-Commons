package com.simplemobiletools.camera.dialogs

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.simplemobiletools.camera.R
import com.simplemobiletools.camera.extensions.config
import com.simplemobiletools.camera.models.MySize
import com.simplemobiletools.camera.models.VideoQuality
import com.simplemobiletools.commons.dialogs.RadioGroupDialog
import com.simplemobiletools.commons.extensions.getAlertDialogBuilder
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.commons.models.RadioItem
import kotlinx.android.synthetic.main.dialog_change_resolution.view.change_resolution_photo
import kotlinx.android.synthetic.main.dialog_change_resolution.view.change_resolution_photo_holder
import kotlinx.android.synthetic.main.dialog_change_resolution.view.change_resolution_video
import kotlinx.android.synthetic.main.dialog_change_resolution.view.change_resolution_video_holder

class ChangeResolutionDialogX(
    private val activity: Activity,
    private val isFrontCamera: Boolean,
    private val photoResolutions: List<MySize> = listOf(),
    private val videoResolutions: List<VideoQuality>,
    private val callback: () -> Unit,
) {
    private var dialog: AlertDialog? = null
    private val config = activity.config

    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_change_resolution, null).apply {
            setupPhotoResolutionPicker(this)
            setupVideoResolutionPicker(this)
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(R.string.ok, null)
            .apply {
                val titleId = if (isFrontCamera) R.string.front_camera else R.string.back_camera
                activity.setupDialogStuff(view, this, titleId) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun setupPhotoResolutionPicker(view: View) {
        val items = photoResolutions.mapIndexed { index, resolution ->
            val megapixels = resolution.megaPixels
            val aspectRatio = resolution.getAspectRatio(activity)
            RadioItem(index, "${resolution.width} x ${resolution.height}  ($megapixels MP,  $aspectRatio)")
        }
        var selectionIndex = if (isFrontCamera) config.frontPhotoResIndex else config.backPhotoResIndex
        selectionIndex = selectionIndex.coerceAtLeast(0)

        view.change_resolution_photo_holder.setOnClickListener {
            RadioGroupDialog(activity, ArrayList(items), selectionIndex) {
                selectionIndex = it as Int
                view.change_resolution_photo.text = items[selectionIndex].title
                if (isFrontCamera) {
                    config.frontPhotoResIndex = it
                } else {
                    config.backPhotoResIndex = it
                }
                dialog?.dismiss()
                callback.invoke()
            }
        }
        view.change_resolution_photo.text = items.getOrNull(selectionIndex)?.title
    }

    private fun setupVideoResolutionPicker(view: View) {
        val items = videoResolutions.mapIndexed { index, videoQuality ->
            val megapixels = videoQuality.megaPixels
            val aspectRatio = videoQuality.getAspectRatio(activity)
            RadioItem(index, "${videoQuality.width} x ${videoQuality.height}  ($megapixels MP,  $aspectRatio)")
        }

        var selectionIndex = if (isFrontCamera) config.frontVideoResIndex else config.backVideoResIndex
        selectionIndex = selectionIndex.coerceAtLeast(0)

        view.change_resolution_video_holder.setOnClickListener {
            RadioGroupDialog(activity, ArrayList(items), selectionIndex) {
                selectionIndex = it as Int
                val selectedItem = items[selectionIndex]
                view.change_resolution_video.text = selectedItem.title
                if (isFrontCamera) {
                    config.frontVideoResIndex = selectionIndex
                } else {
                    config.backVideoResIndex = selectionIndex
                }
                dialog?.dismiss()
                callback.invoke()
            }
        }
        val selectedItem = items.getOrNull(selectionIndex)
        view.change_resolution_video.text = selectedItem?.title
    }
}