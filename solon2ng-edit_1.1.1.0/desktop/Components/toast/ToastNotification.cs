
using System;

using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace solonng_launcher.controls.toast
{
     partial class ToastNotification : Form
    {
        public ToastNotification()
        {
            InitializeComponent();
            this.TopMost = true;
        }

       
        private ToastAction action;

        private int x, y;

        private void Button1_Click(object sender, EventArgs e)
        {
            
        }

        private void Timer1_Tick(object sender, EventArgs e)
        {
            switch(this.action)
            {
                case ToastAction.wait:
                    timer1.Interval = 5000;
                    action = ToastAction.close;
                    break;
                case ToastAction.start:
                    this.timer1.Interval = 1;
                    this.Opacity += 0.1;
                    if (this.x < this.Location.X)
                    {
                        this.Left--;
                    }
                    else
                    {
                        if (this.Opacity == 1.0)
                        {
                            action = ToastAction.wait;
                        }
                    }
                    break;
                case ToastAction.close:
                    timer1.Interval = 1;
                    this.Opacity -= 0.1;

                    this.Left -= 3;
                    if (this.Opacity == 0.0)
                    {
                        this.Close();
                    }
                    break;
            }
        }

        private void PictureBox2_Click(object sender, EventArgs e)
        {
            timer1.Interval = 1;
            action = ToastAction.close;
        }

        public void ShowAlert(string msg, ToastType type)
        {
            this.Opacity = 0.0;
            this.StartPosition = FormStartPosition.Manual;
            string fname;

            for (int i = 1; i < 20; i++)
            {
                fname = "alert" + i.ToString();
                ToastNotification frm = (ToastNotification)Application.OpenForms[fname];

              
                    this.Name = fname;
                    this.x = Screen.PrimaryScreen.WorkingArea.Width - this.Width + 15;
                    this.y = Screen.PrimaryScreen.WorkingArea.Height - this.Height * i - 5 * i;
                    this.Location = new Point(this.x, this.y);
                if (frm == null)
                {
                    break;
                }

            }
            this.x = Screen.PrimaryScreen.WorkingArea.Width - base.Width - 5;

            switch(type)
            {
                case ToastType.Success:
                    this.BackColor = Color.SeaGreen;
                    break;
                case ToastType.Error:
                    this.BackColor = Color.DarkRed;
                    break;
                case ToastType.Info:
                    this.BackColor = Color.RoyalBlue;
                    break;
                case ToastType.Warning:
                    this.BackColor = Color.DarkOrange;
                    break;
            }

            var strBuilder = new StringBuilder();
            strBuilder.Append(msg);
            strBuilder.Append(Environment.NewLine);
            strBuilder.Append(" ");
            this.lblMsg.Text = strBuilder.ToString();
            this.Show();
            this.action = ToastAction.start;
            this.timer1.Interval = 1;
            this.timer1.Start();
        }

        public static void Show(string msg, ToastType type)
        {
            var toastNotification = new ToastNotification();
            toastNotification.ShowAlert(msg, type);
        }
    }
}
