using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace solonng_launcher.controls.modal
{
     partial class SolonModal : Form
    {


        public const int WM_NCLBUTTONDOWN = 0xA1;
        public const int HT_CAPTION = 0x2;

        [DllImportAttribute("user32.dll")]
        public static extern int SendMessage(IntPtr hWnd, int Msg, int wParam, int lParam);
        [DllImportAttribute("user32.dll")]
        public static extern bool ReleaseCapture();

        private int _initSize = 0;
        public SolonModal(string message, string title, string action1Text, string action2Text, string action3Text)
        {


            InitializeComponent();
            this.TopMost = true;
            _initSize = lblMessage.Height;
            lblTitle.Text = title;
            lblMessage.Text = message;

            //var robotSlabFontCollection = App_Code.utils.FontsHelper.RobotSlabFontCollection;

            btnAction1.DialogResult = DialogResult.OK;
            btnAction1.Visible = action1Text.Length != 0;
            //btnAction1.Font = new System.Drawing.Font(robotSlabFontCollection.Families[0], btnAction1.Font.Size);
            //btnAction1.UseCompatibleTextRendering = true;
            btnAction1.Text = action1Text;


            btnAction2.DialogResult = DialogResult.No;
            btnAction2.Visible = action2Text.Length != 0;
            //btnAction2.Font = new System.Drawing.Font(robotSlabFontCollection.Families[0], btnAction2.Font.Size);
            btnAction2.UseCompatibleTextRendering = true;
            btnAction2.Text = action2Text;


            btnAction3.DialogResult = DialogResult.Abort;
            btnAction3.Visible = action3Text.Length != 0;
            //btnAction3.Font = new System.Drawing.Font(robotSlabFontCollection.Families[0], btnAction3.Font.Size);
            btnAction3.Text = action3Text;
            btnClose.DialogResult = DialogResult.Ignore;
            //btnAction3.UseCompatibleTextRendering = true;

            //lblMessage.Font = new System.Drawing.Font(robotSlabFontCollection.Families[0], lblMessage.Font.Size);
            //lblMessage.UseCompatibleTextRendering = true;

            //lblTitle.Font = new System.Drawing.Font(robotSlabFontCollection.Families[0], lblTitle.Font.Size);
            //lblTitle.UseCompatibleTextRendering = true;


        }

     
        public static ModalDialogResult Show(string message, string title, string action1Text = "", string action2Text= "", string action3Text= "")
        {

            var modalForm = new SolonModal(message, title,action1Text,action2Text,action3Text);
           
            modalForm.Focus();
            var dialogueResult = modalForm.ShowDialog();
            switch (dialogueResult)
            {
                case DialogResult.OK:
                    return ModalDialogResult.Action1;
                case DialogResult.No:
                    return ModalDialogResult.Action2;
                case DialogResult.Abort:
                    return ModalDialogResult.Action3;
                case DialogResult.Ignore:
                    return ModalDialogResult.Close;
            }

            return ModalDialogResult.Null;


        }

     
        private void BtnAction_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void BtnClose_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void MainPanel_MouseMove(object sender, MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Left)
            {
                ReleaseCapture();
                SendMessage(Handle, WM_NCLBUTTONDOWN, HT_CAPTION, 0);
            }
        }
    }
}
