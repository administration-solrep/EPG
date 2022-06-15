using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.Diagnostics;
using System.Reflection;
using solonng_launcher.utils;
using solonng_launcher.Resources;
using solonng_launcher.controls.modal;
using System.Drawing.Text;
using System.Linq;
using System.IO;

namespace solonng_launcher
{
     partial class MainWindow : Form
    {
        #region "properties"
        private delegate void UpdateTextMessage(string aMessage);
        private delegate void RefreshMainDataGrid();
        private delegate void ShowNotification(string content, controls.toast.ToastType notificationType);
        private const int ACTION_CELL_INDEX = 2;
        private string _selectedDocumentId = "";
        private readonly string _assemblyVersion = "";
        #endregion
 

        public MainWindow()
        {
            InitializeComponent();
            var assembly = Assembly.GetExecutingAssembly();
            var fvi = FileVersionInfo.GetVersionInfo(assembly.Location);
            _assemblyVersion = fvi.FileVersion;
            var robotSlabFontCollection = App_Code.utils.FontsHelper.RobotSlabFontCollection;
            lblAppTitle.Font = new Font(robotSlabFontCollection.Families[0], lblAppTitle.Font.Size);
            
            lblAppTitle.UseCompatibleTextRendering = true;

            lblSectionTitle.Font = new  Font(robotSlabFontCollection.Families[0], lblSectionTitle.Font.Size);
            lblSectionTitle.UseCompatibleTextRendering = true;

            lblMessage.Font = new  Font(robotSlabFontCollection.Families[0], lblMessage.Font.Size);
            lblMessage.UseCompatibleTextRendering = true;
            lblAssemblyVersion.Text = $"Version: { _assemblyVersion}";
            lblAssemblyVersion.Font = new Font(robotSlabFontCollection.Families[0], 9F, FontStyle.Italic, GraphicsUnit.Point, ((byte)(0)));




        }

        #region "Helper methods"

        /// <summary>
        /// Refresh the datagrid view the files in the file context index
        /// </summary>
        public void SafeRefreshMainDataGrid()
        {


            if (mainDataGridView.InvokeRequired)
            {
                var d = new RefreshMainDataGrid(SafeRefreshMainDataGrid);
                mainDataGridView.Invoke(d);
            }
            else
            {
                var currentFiles = GlobalContext.Instance.FileContextIndex;
                mainDataGridView.Rows.Clear();
                var isFilesToProcessExists = currentFiles.Count != 0;
                mainDataGridView.Visible = isFilesToProcessExists;
                lblMessage.Visible = !isFilesToProcessExists;
                if (!isFilesToProcessExists)
                {
                    return;
                }
                if (mainDataGridView.ColumnCount == 0)
                {
                    return;
                }

                //var robotSlabFontCollection = App_Code.utils.FontsHelper.RobotSlabFontCollection;
                int selectedRowIndex = -1, selectedColumnIndex = -1;
                
                foreach (KeyValuePair<string, FileContext> entry in currentFiles)
                {

                    var fileContext = entry.Value;
                    
                    mainDataGridView.Rows.Add();
                    var rowIndex = mainDataGridView.RowCount - 1;
                    var dgvR = mainDataGridView.Rows[rowIndex];
                    var btnAction = new DataGridViewImageColumn();
                    
                    dgvR.Cells["DocumentId"].Value = fileContext.DocumentId;
                    dgvR.Cells["FileName"].Value = fileContext.Filename;
                    dgvR.Cells["Status"].Value = fileContext.Statut.Label;
                    Image image;
                    if (fileContext.Statut.Equal(Constants.STATUT_LOCKED))
                    {
                        image = Image.FromFile($"{AppDomain.CurrentDomain.BaseDirectory}/Resources/images/trash.png");

                    }
                    else if (fileContext.Statut.Equal(Constants.STATUT_DOWNLOAD))
                    {
                        image = Image.FromFile($"{AppDomain.CurrentDomain.BaseDirectory}/Resources/images/warning.png");

                    }
                    else if (fileContext.Statut.Equal(Constants.STATUT_SAVED) || fileContext.Statut.Equal(Constants.STATUT_PRE_EDIT))
                    {
                        image = Image.FromFile($"{AppDomain.CurrentDomain.BaseDirectory}/Resources/images/loading.gif");
                    }
                    else
                    {
                        image = Image.FromFile($"{AppDomain.CurrentDomain.BaseDirectory}/Resources/images/file_edit.png");
                    }

                   
                    dgvR.Cells["Action"].Value = image;

                    
                }
                mainDataGridView.ClearSelection();
                if (selectedColumnIndex != -1 && selectedRowIndex != -1)
                    mainDataGridView.Rows[selectedRowIndex].Cells[selectedColumnIndex].Selected = true;
            }


        }

     
        /// <summary>
        /// Show a notification
        /// </summary>
        /// <param name="content"></param>
        /// <param name="notificationType"></param>
        public void SafeShowNotification(string content, controls.toast.ToastType notificationType)
        {
            if (this.InvokeRequired)
            {
                var d = new ShowNotification(SafeShowNotification);
                this.Invoke(d, new object[] {  content, notificationType });
            }
            else
            {
                this.Focus();
                controls.toast.ToastNotification.Show(content, notificationType);
            }

        }

        /// <summary>
        /// Exit the application
        /// </summary>
        public void Exit()
        {
            Application.Exit();
            
            Environment.Exit(0);
        }

        /// <summary>
        /// Change the cursor when user hover an action icon in datagridview
        /// </summary>
        /// <param name="cell"></param>
        private void ChangeCursorOnActionDeleteCellHover(DataGridViewCell cell)
        {

            try
            {
                string documentId = cell.DataGridView.Rows[cell.RowIndex].Cells["DocumentId"].Value.ToString();
                if (cell.ColumnIndex == ACTION_CELL_INDEX && GlobalContext.Instance.FileContextIndex[documentId].Statut.Equal(Constants.STATUT_LOCKED))
                {
                    Cursor.Current = Cursors.Hand;

                }
            }
            catch (Exception) { }
        }
        #endregion

        #region "Events"
        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (e.CloseReason == CloseReason.ApplicationExitCall)
            {
                return;
            }
            ModalDialogResult result;
            var globalContext = GlobalContext.Instance;
            if (globalContext.CurrentFilesHavePendingFile())
            {
                result = SolonModal.Show(ApplicationMessages.ConfirmApplicationCloseWithPendingFiles, "Confirmation"
                                      , "Annuler", "", "Valider");
            }
            else
            {
                 result = SolonModal.Show(ApplicationMessages.ConfirmApplicationClose, "Confirmation"
                       , "Annuler", "", "Valider");
            }

            if (result == ModalDialogResult.Action3)
            {
                foreach (KeyValuePair<string, FileContext> keyValuePair in globalContext.FileContextIndex){
                    var fileContext = keyValuePair.Value;
                    if (fileContext.Statut.Equal(Constants.STATUT_EDITING) || fileContext.Statut.Equal(Constants.STATUT_SAVED) || fileContext.Statut.Equal(Constants.STATUT_LOCKED)  )
                    {
                        globalContext.ApiHandler.NotifyFileIsNotBeingEdited(fileContext);
                    }

                }
                LogHelper.LogInformation(LogMessages.ApplicationClosedInfo);
            }
            else
            {
                e.Cancel = true;
            }
            
        }
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            var globalContext = GlobalContext.Instance;

            
            notifyIcon1.Dispose();
            globalContext.LauncherManager.ExitApplication();
        }


        private void Btn_document_Click(object sender, EventArgs e)
        {
            if (Directory.Exists(Constants.SolonDocumentFolder))
            {
                Process.Start(Constants.SolonDocumentFolder);
            }
        }

  

        private void NotifyIcon1_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            Show();
            this.WindowState = FormWindowState.Normal;
            notifyIcon1.Visible = true;
        }

    



        private void MainDataGridView_CellMouseEnter(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex == -1)
            {
                return;
            }
            var currentCell = mainDataGridView.Rows[e.RowIndex].Cells[e.ColumnIndex];
            this.ChangeCursorOnActionDeleteCellHover(currentCell);
    
        }

     

        private void MainDataGridView_SelectionChanged(object sender, EventArgs e)
        {
            //mainDataGridView.ClearSelection();
        }

        private void MainDataGridView_CellMouseMove(object sender, DataGridViewCellMouseEventArgs e)
        {
            try
            {
                if (e.RowIndex == -1)
                {
                    return;
                }
                var currentCell = mainDataGridView.Rows[e.RowIndex].Cells[e.ColumnIndex];
                this.ChangeCursorOnActionDeleteCellHover(currentCell);
            }
            catch (Exception) {  }
        }

       

        private void MainDataGridView_CellMouseDown(object sender, DataGridViewCellMouseEventArgs e)
        {
            if (e.RowIndex == -1)
            {
                return;
            }
            try
            {
                string documentId = mainDataGridView.Rows[e.RowIndex].Cells["DocumentId"].Value.ToString();
                var globalContext = GlobalContext.Instance;
                var currDocument = globalContext.FileContextIndex[documentId];
                if (e.Button == MouseButtons.Left)
                {
                    if (e.ColumnIndex == ACTION_CELL_INDEX && currDocument.Statut.Equal(Constants.STATUT_LOCKED))
                    {
                        var result = SolonModal.Show(string.Format(ApplicationMessages.FileDeleteConfirm, currDocument.Filename), "Confirmation"
                    , "Annuler", "", "Supprimer");

                        if (result == ModalDialogResult.Action3)
                        {
                            lock (globalContext.FileContextIndex)
                            {
                                globalContext.RemoveFile(documentId);
                            }
                        }
                    }
                }
                else if (e.Button == MouseButtons.Right)
                {
                    deleteToolStripMenuItem.Enabled = currDocument.Statut.Equal(Constants.STATUT_LOCKED) ||currDocument.Statut.Equal(Constants.STATUT_NOT_FOUND) ;
                    reUploadToolStripMenuItem.Enabled = currDocument.Statut.Equal(Constants.STATUT_LOCKED) || currDocument.Statut.Equal(Constants.STATUT_SAVED);
                        
                    mainDataGridView.ClearSelection();
                    _selectedDocumentId = documentId;
                    var cursorY = mainDataGridView.CurrentRow.Height *  (e.RowIndex+2);
                    var cursorX = (Cursor.Position.X - this.Bounds.X - mainDataGridView.Bounds.X) - 10;
                    mainDataGridView.Rows[e.RowIndex].Selected = true;
                    contextMenuDataGridView.Show(mainDataGridView, new Point(cursorX, cursorY));
                    
                }
            }
            catch (Exception) { }

        }

        private void NotifyIcon1_MouseClick(object sender, MouseEventArgs e)
        {

            if (e.Button == MouseButtons.Left)
            {
                MethodInfo mi = typeof(NotifyIcon).GetMethod("ShowContextMenu", BindingFlags.Instance | BindingFlags.NonPublic);
                mi.Invoke(notifyIcon1, null);
            }
        }

        private void MENU_info_Click(object sender, EventArgs e)
        {
           
            SolonModal.Show($"À propos Version: {_assemblyVersion} " +
                $"\n\rEnvironement: {Environment.Version} " +
                $"\n\r.Net: {System.Runtime.InteropServices.RuntimeEnvironment.GetSystemVersion()} " +
                 $"\n\r{Environment.OSVersion.VersionString} " 
                , "Informations","","OK");
        }

        private void MENU_quit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void AccésAuFichierDeTracesToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (Directory.Exists(Constants.SolonLogFolder))
            {
                Process.Start(Constants.SolonLogFolder);
            }
        }

        private void ReUploadToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(_selectedDocumentId))
            {
                return;
            }
            
            var globalContext = GlobalContext.Instance;
            var currDocument = globalContext.FileContextIndex[_selectedDocumentId];
            lock (currDocument.lockFlag)
            {
                
               
                currDocument.CanShowErrors = true;
                currDocument.CanShowLockedError = true;
                currDocument.Statut = Constants.STATUT_SAVED;
                currDocument.ReUploadCounter = 0;
            }
        }

        private void DeleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(_selectedDocumentId))
            {
                return;
            }

            var globalContext = GlobalContext.Instance;
            var currDocument = globalContext.FileContextIndex[_selectedDocumentId];
            var result = SolonModal.Show(string.Format(ApplicationMessages.FileDeleteConfirm, currDocument.Filename), "Confirmation"
                 , "Annuler", "", "Supprimer");

            if (result == ModalDialogResult.Action3)
            {
                lock (globalContext.FileContextIndex)
                {
                    globalContext.RemoveFile(_selectedDocumentId);
                }
            }
        }


        private void MainWindow_MouseClick(object sender, MouseEventArgs e)
        {
            mainDataGridView.ClearSelection();
        }

        #endregion

        private void MainDataGridView_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                var selectedCell = mainDataGridView.CurrentCell;
                if (selectedCell != null && selectedCell.ColumnIndex == ACTION_CELL_INDEX)
                {
                    string documentId = mainDataGridView.Rows[mainDataGridView.CurrentRow.Index].Cells["DocumentId"].Value.ToString();
                    var globalContext = GlobalContext.Instance;
                    var currDocument = globalContext.FileContextIndex[documentId];
                    if (currDocument.Statut.Equal(Constants.STATUT_LOCKED))
                    {
                       

                        var result = SolonModal.Show(string.Format(ApplicationMessages.FileDeleteConfirm, currDocument.Filename), "Confirmation"
                    , "Annuler", "", "Supprimer");

                        if (result == ModalDialogResult.Action3)
                        {
                            lock (globalContext.FileContextIndex)
                            {
                                globalContext.RemoveFile(documentId);
                            }
                        }
                    }
                }
               
            }
           
        }
    }
}
