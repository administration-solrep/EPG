
namespace solonng_launcher.controls.modal
{
    partial class SolonModal
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SolonModal));
            this.btnAction1 = new System.Windows.Forms.Button();
            this.btnAction2 = new System.Windows.Forms.Button();
            this.btnAction3 = new System.Windows.Forms.Button();
            this.lblMessage = new System.Windows.Forms.Label();
            this.mainPanel = new System.Windows.Forms.Panel();
            this.btnClose = new System.Windows.Forms.Button();
            this.lblTitle = new System.Windows.Forms.Label();
            this.panel2 = new System.Windows.Forms.Panel();
            this.mainPanel.SuspendLayout();
            this.panel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnAction1
            // 
            this.btnAction1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnAction1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction1.Cursor = System.Windows.Forms.Cursors.Hand;
            this.btnAction1.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnAction1.Font = new System.Drawing.Font("Arial", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnAction1.ForeColor = System.Drawing.Color.White;
            this.btnAction1.Location = new System.Drawing.Point(14, 204);
            this.btnAction1.Name = "btnAction1";
            this.btnAction1.Size = new System.Drawing.Size(89, 32);
            this.btnAction1.TabIndex = 0;
            this.btnAction1.Text = "Action1";
            this.btnAction1.UseVisualStyleBackColor = false;
            this.btnAction1.Click += new System.EventHandler(this.BtnAction_Click);
            // 
            // btnAction2
            // 
            this.btnAction2.Anchor = System.Windows.Forms.AnchorStyles.Bottom;
            this.btnAction2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction2.Cursor = System.Windows.Forms.Cursors.Hand;
            this.btnAction2.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction2.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnAction2.Font = new System.Drawing.Font("Arial", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnAction2.ForeColor = System.Drawing.Color.White;
            this.btnAction2.Location = new System.Drawing.Point(191, 204);
            this.btnAction2.Name = "btnAction2";
            this.btnAction2.Size = new System.Drawing.Size(90, 32);
            this.btnAction2.TabIndex = 1;
            this.btnAction2.Text = "Action2";
            this.btnAction2.UseVisualStyleBackColor = false;
            this.btnAction2.Click += new System.EventHandler(this.BtnAction_Click);
            // 
            // btnAction3
            // 
            this.btnAction3.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.btnAction3.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction3.Cursor = System.Windows.Forms.Cursors.Hand;
            this.btnAction3.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnAction3.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnAction3.Font = new System.Drawing.Font("Arial", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnAction3.ForeColor = System.Drawing.Color.White;
            this.btnAction3.Location = new System.Drawing.Point(373, 204);
            this.btnAction3.Name = "btnAction3";
            this.btnAction3.Size = new System.Drawing.Size(89, 32);
            this.btnAction3.TabIndex = 2;
            this.btnAction3.Text = "Action3";
            this.btnAction3.UseVisualStyleBackColor = false;
            this.btnAction3.Click += new System.EventHandler(this.BtnAction_Click);
            // 
            // lblMessage
            // 
            this.lblMessage.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.lblMessage.Font = new System.Drawing.Font("Arial", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblMessage.ImageAlign = System.Drawing.ContentAlignment.TopCenter;
            this.lblMessage.Location = new System.Drawing.Point(17, 48);
            this.lblMessage.Name = "lblMessage";
            this.lblMessage.Size = new System.Drawing.Size(451, 142);
            this.lblMessage.TabIndex = 3;
            this.lblMessage.Text = "Le fichier existe déjà.\r\n Voulez vous le concerver ou télécharger une nouvelle ve" +
    "rsion.\r\nCliquer sur abondonner pour annuler.";
            this.lblMessage.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            // 
            // mainPanel
            // 
            this.mainPanel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.mainPanel.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.mainPanel.Controls.Add(this.btnClose);
            this.mainPanel.Controls.Add(this.lblTitle);
            this.mainPanel.Location = new System.Drawing.Point(0, 0);
            this.mainPanel.Name = "mainPanel";
            this.mainPanel.Size = new System.Drawing.Size(475, 46);
            this.mainPanel.TabIndex = 4;
            this.mainPanel.MouseMove += new System.Windows.Forms.MouseEventHandler(this.MainPanel_MouseMove);
            // 
            // btnClose
            // 
            this.btnClose.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnClose.Cursor = System.Windows.Forms.Cursors.Hand;
            this.btnClose.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btnClose.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnClose.Font = new System.Drawing.Font("Century Gothic", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnClose.ForeColor = System.Drawing.Color.White;
            this.btnClose.Location = new System.Drawing.Point(426, 5);
            this.btnClose.Name = "btnClose";
            this.btnClose.Size = new System.Drawing.Size(45, 36);
            this.btnClose.TabIndex = 4;
            this.btnClose.Text = "x";
            this.btnClose.UseVisualStyleBackColor = true;
            this.btnClose.Click += new System.EventHandler(this.BtnClose_Click);
            // 
            // lblTitle
            // 
            this.lblTitle.Font = new System.Drawing.Font("Arial", 11.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblTitle.ForeColor = System.Drawing.Color.White;
            this.lblTitle.ImageAlign = System.Drawing.ContentAlignment.BottomLeft;
            this.lblTitle.Location = new System.Drawing.Point(15, 1);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(137, 45);
            this.lblTitle.TabIndex = 5;
            this.lblTitle.Text = "Confirmation";
            this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.lblTitle.MouseDown += new System.Windows.Forms.MouseEventHandler(this.MainPanel_MouseMove);
            // 
            // panel2
            // 
            this.panel2.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panel2.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panel2.Controls.Add(this.btnAction2);
            this.panel2.Controls.Add(this.btnAction3);
            this.panel2.Controls.Add(this.btnAction1);
            this.panel2.Controls.Add(this.lblMessage);
            this.panel2.Location = new System.Drawing.Point(0, 0);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(475, 256);
            this.panel2.TabIndex = 5;
            // 
            // SolonModal
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(475, 256);
            this.Controls.Add(this.mainPanel);
            this.Controls.Add(this.panel2);
            this.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "SolonModal";
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.mainPanel.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnAction1;
        private System.Windows.Forms.Button btnAction2;
        private System.Windows.Forms.Button btnAction3;
        private System.Windows.Forms.Label lblMessage;
        private System.Windows.Forms.Panel mainPanel;
        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Button btnClose;
    }
}