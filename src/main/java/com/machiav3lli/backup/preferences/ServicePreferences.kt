package com.machiav3lli.backup.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.machiav3lli.backup.COMPRESSION_TYPES
import com.machiav3lli.backup.OABX
import com.machiav3lli.backup.R
import com.machiav3lli.backup.dialogs.BaseDialog
import com.machiav3lli.backup.dialogs.EnumPrefDialogUI
import com.machiav3lli.backup.dialogs.ListPrefDialogUI
import com.machiav3lli.backup.dialogs.StringPrefDialogUI
import com.machiav3lli.backup.entity.BooleanPref
import com.machiav3lli.backup.entity.EnumPref
import com.machiav3lli.backup.entity.IntPref
import com.machiav3lli.backup.entity.ListPref
import com.machiav3lli.backup.entity.PasswordPref
import com.machiav3lli.backup.entity.Pref
import com.machiav3lli.backup.entity.StringPref
import com.machiav3lli.backup.preferences.ui.PrefsGroup
import com.machiav3lli.backup.ui.compose.icons.Phosphor
import com.machiav3lli.backup.ui.compose.icons.phosphor.FileZip
import com.machiav3lli.backup.ui.compose.icons.phosphor.FloppyDisk
import com.machiav3lli.backup.ui.compose.icons.phosphor.GameController
import com.machiav3lli.backup.ui.compose.icons.phosphor.Hash
import com.machiav3lli.backup.ui.compose.icons.phosphor.Key
import com.machiav3lli.backup.ui.compose.icons.phosphor.Password
import com.machiav3lli.backup.ui.compose.icons.phosphor.PlayCircle
import com.machiav3lli.backup.ui.compose.icons.phosphor.Prohibit
import com.machiav3lli.backup.ui.compose.icons.phosphor.ProhibitInset
import com.machiav3lli.backup.ui.compose.icons.phosphor.ShieldCheckered
import com.machiav3lli.backup.ui.compose.icons.phosphor.ShieldStar
import com.machiav3lli.backup.ui.compose.icons.phosphor.TagSimple
import com.machiav3lli.backup.ui.compose.icons.phosphor.Textbox
import com.machiav3lli.backup.ui.compose.recycler.InnerBackground
import com.machiav3lli.backup.ui.compose.theme.ColorAPK
import com.machiav3lli.backup.ui.compose.theme.ColorData
import com.machiav3lli.backup.ui.compose.theme.ColorDeData
import com.machiav3lli.backup.ui.compose.theme.ColorExodus
import com.machiav3lli.backup.ui.compose.theme.ColorExtDATA
import com.machiav3lli.backup.ui.compose.theme.ColorMedia
import com.machiav3lli.backup.ui.compose.theme.ColorOBB
import com.machiav3lli.backup.ui.compose.theme.ColorSpecial
import com.machiav3lli.backup.ui.compose.theme.ColorUpdated
import com.machiav3lli.backup.utils.SystemUtils
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ServicePrefsPage() {
    val openDialog = remember { mutableStateOf(false) }
    var dialogsPref by remember { mutableStateOf<Pref?>(null) }

    InnerBackground(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ServicePrefGroups { pref ->
                dialogsPref = pref
                openDialog.value = true
            }
        }
    }
}

fun LazyListScope.ServicePrefGroups(onPrefDialog: (Pref) -> Unit) {
    val generalServicePrefs = Pref.prefGroups["srv"]?.toPersistentList() ?: persistentListOf()
    val backupServicePrefs = Pref.prefGroups["srv-bkp"]?.toPersistentList() ?: persistentListOf()
    val restoreServicePrefs = Pref.prefGroups["srv-rst"]?.toPersistentList() ?: persistentListOf()

    item {
        PrefsGroup(
            prefs = generalServicePrefs,
            onPrefDialog = onPrefDialog
        )
    }
    item {
        PrefsGroup(
            prefs = backupServicePrefs,
            heading = stringResource(id = R.string.backup),
            onPrefDialog = onPrefDialog
        )
    }
    item {
        PrefsGroup(
            prefs = restoreServicePrefs,
            heading = stringResource(id = R.string.restore),
            onPrefDialog = onPrefDialog
        )
    }
}

val pref_encryption = BooleanPref(
    key = "srv.encryption",
    titleId = R.string.prefs_encryption,
    summaryId = R.string.prefs_encryption_summary,
    icon = Phosphor.Key,
    defaultValue = false
)


val pref_password = PasswordPref(
    key = "srv.password",
    titleId = R.string.prefs_password,
    summaryId = R.string.prefs_password_summary,
    icon = Phosphor.Password,
    iconTint = {
        val pref = it as PasswordPref
        if (pref.value.isNotEmpty()) Color.Green else Color.Gray
    },
    defaultValue = "",
)

val kill_password = PasswordPref(   // make sure password is never saved in non-encrypted prefs
    key = "kill.password",
    private = false,
    defaultValue = ""
)
val kill_password_set = run { kill_password.value = "" }


val pref_backupDeviceProtectedData = BooleanPref(
    key = "srv-bkp.backupDeviceProtectedData",
    titleId = R.string.prefs_deviceprotecteddata,
    summaryId = R.string.prefs_deviceprotecteddata_summary,
    icon = Phosphor.ShieldCheckered,
    defaultValue = true
)

val pref_backupExternalData = BooleanPref(
    key = "srv-bkp.backupExternalData",
    titleId = R.string.prefs_externaldata,
    summaryId = R.string.prefs_externaldata_summary,
    icon = Phosphor.FloppyDisk,
    defaultValue = true
)

val pref_backupObbData = BooleanPref(
    key = "srv-bkp.backupObbData",
    titleId = R.string.prefs_obbdata,
    summaryId = R.string.prefs_obbdata_summary,
    icon = Phosphor.GameController,
    defaultValue = true
)

val pref_backupMediaData = BooleanPref(
    key = "srv-bkp.backupMediaData",
    titleId = R.string.prefs_mediadata,
    summaryId = R.string.prefs_mediadata_summary,
    icon = Phosphor.PlayCircle,
    defaultValue = true
)

val pref_backupNoBackupData = BooleanPref(
    key = "srv-bkp.backupNoBackupData",
    titleId = R.string.prefs_nobackupdata,
    summaryId = R.string.prefs_nobackupdata_summary,
    icon = Phosphor.ProhibitInset,
    defaultValue = false,
    onChanged = { OABX.assets.updateExcludeFiles() },
)

val pref_backupCache = BooleanPref(
    key = "srv-bkp.backupCache",
    titleId = R.string.prefs_backupcache,
    summaryId = R.string.prefs_backupcache_summary,
    icon = Phosphor.Prohibit,
    defaultValue = false
)

val pref_restoreDeviceProtectedData = BooleanPref(
    key = "srv-rst.restoreDeviceProtectedData",
    titleId = R.string.prefs_deviceprotecteddata_rst,
    summaryId = R.string.prefs_deviceprotecteddata_rst_summary,
    icon = Phosphor.ShieldCheckered,
    defaultValue = true
)

val pref_restoreExternalData = BooleanPref(
    key = "srv-rst.restoreExternalData",
    titleId = R.string.prefs_externaldata_rst,
    summaryId = R.string.prefs_externaldata_rst_summary,
    icon = Phosphor.FloppyDisk,
    defaultValue = true
)

val pref_restoreObbData = BooleanPref(
    key = "srv-rst.restoreObbData",
    titleId = R.string.prefs_obbdata_rst,
    summaryId = R.string.prefs_obbdata_rst_summary,
    icon = Phosphor.GameController,
    defaultValue = true
)

val pref_restoreMediaData = BooleanPref(
    key = "srv-rst.restoreMediaData",
    titleId = R.string.prefs_mediadata_rst,
    summaryId = R.string.prefs_mediadata_rst_summary,
    icon = Phosphor.PlayCircle,
    defaultValue = true
)

val pref_restoreNoBackupData = BooleanPref(
    key = "srv-rst.restoreNoBackupData",
    titleId = R.string.prefs_nobackupdata_rst,
    summaryId = R.string.prefs_nobackupdata_rst_summary,
    icon = Phosphor.ProhibitInset,
    defaultValue = false,
    onChanged = { OABX.assets.updateExcludeFiles() },
)

val pref_restoreCache = BooleanPref(
    key = "srv-rst.restoreCache",
    titleId = R.string.prefs_restorecache,
    summaryId = R.string.prefs_restorecache_summary,
    icon = Phosphor.Prohibit,
    defaultValue = false
)

val pref_restorePermissions = BooleanPref(
    key = "srv.restorePermissions",
    titleId = R.string.prefs_restorepermissions,
    summaryId = R.string.prefs_restorepermissions_summary,
    icon = Phosphor.ShieldStar,
    defaultValue = true
)

val pref_numBackupRevisions = IntPref(
    key = "srv.numBackupRevisions",
    titleId = R.string.prefs_numBackupRevisions,
    summaryId = R.string.prefs_numBackupRevisions_summary,
    icon = Phosphor.Hash,
    entries = ((0..9) + (10..20 step 2) + (50..200 step 50)).toList(),
    defaultValue = 2
)

val pref_compressionType = ListPref(
    key = "srv.compressionType",
    titleId = R.string.prefs_compression_type,
    summaryId = R.string.prefs_compression_type_summary,
    icon = Phosphor.FileZip,
    entries = COMPRESSION_TYPES,
    defaultValue = "zst"
)

val pref_compressionLevel = IntPref(
    key = "srv.compressionLevel",
    titleId = R.string.prefs_compression_level,
    summaryId = R.string.prefs_compression_level_summary,
    icon = Phosphor.FileZip,
    entries = (0..9).toList(),
    defaultValue = 2
)

val pref_enableSessionInstaller = BooleanPref(
    key = "srv.enableSessionInstaller",
    titleId = R.string.prefs_sessionIinstaller,
    summaryId = R.string.prefs_sessionIinstaller_summary,
    icon = Phosphor.TagSimple,
    defaultValue = true
)

val pref_installationPackage = StringPref(
    key = "srv.installationPackage",
    titleId = R.string.prefs_installerpackagename,
    icon = Phosphor.Textbox,
    defaultValue = SystemUtils.packageName
)
