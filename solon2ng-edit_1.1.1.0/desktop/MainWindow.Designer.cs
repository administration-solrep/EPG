namespace solonng_launcher
{
    partial class MainWindow
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainWindow));
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle5 = new System.Windows.Forms.DataGridViewCellStyle();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle6 = new System.Windows.Forms.DataGridViewCellStyle();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.notifyIcon1 = new System.Windows.Forms.NotifyIcon(this.components);
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.accésAuFichierDeTracesToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.MENU_info = new System.Windows.Forms.ToolStripMenuItem();
            this.MENU_quit = new System.Windows.Forms.ToolStripMenuItem();
            this.mainDataGridView = new System.Windows.Forms.DataGridView();
            this.FileName = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Status = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Action = new System.Windows.Forms.DataGridViewImageColumn();
            this.DocumentId = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.header = new System.Windows.Forms.Panel();
            this.btn_document_Click = new System.Windows.Forms.Button();
            this.lblAppTitle = new System.Windows.Forms.Label();
            this.lblSectionTitle = new System.Windows.Forms.Label();
            this.mainWindowToolTip = new System.Windows.Forms.ToolTip(this.components);
            this.lblMessage = new System.Windows.Forms.Label();
            this.contextMenuDataGridView = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.reUploadToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.deleteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.lblAssemblyVersion = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.contextMenuStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.mainDataGridView)).BeginInit();
            this.header.SuspendLayout();
            this.contextMenuDataGridView.SuspendLayout();
            this.SuspendLayout();
            // 
            // pictureBox1
            // 
            this.pictureBox1.BackColor = System.Drawing.Color.Transparent;
            this.pictureBox1.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.pictureBox1.Image = global::solonng_launcher.Properties.Resources.logopm;
            this.pictureBox1.Location = new System.Drawing.Point(15, 7);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(85, 85);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.pictureBox1.TabIndex = 0;
            this.pictureBox1.TabStop = false;
            this.pictureBox1.MouseClick += new System.Windows.Forms.MouseEventHandler(this.MainWindow_MouseClick);
            // 
            // notifyIcon1
            // 
            this.notifyIcon1.BalloonTipIcon = System.Windows.Forms.ToolTipIcon.Info;
            this.notifyIcon1.BalloonTipText = "Editeur de texte Solon EPG";
            this.notifyIcon1.BalloonTipTitle = "Solon-edit";
            this.notifyIcon1.ContextMenuStrip = this.contextMenuStrip1;
            this.notifyIcon1.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon1.Icon")));
            this.notifyIcon1.Text = "Solon-edit";
            this.notifyIcon1.Visible = true;
            this.notifyIcon1.MouseClick += new System.Windows.Forms.MouseEventHandler(this.NotifyIcon1_MouseClick);
            this.notifyIcon1.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.NotifyIcon1_MouseDoubleClick);
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.ImageScalingSize = new System.Drawing.Size(40, 40);
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.accésAuFichierDeTracesToolStripMenuItem,
            this.MENU_info,
            this.MENU_quit});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(160, 70);
            // 
            // accésAuFichierDeTracesToolStripMenuItem
            // 
            this.accésAuFichierDeTracesToolStripMenuItem.Image = global::solonng_launcher.Properties.Resources.search;
            this.accésAuFichierDeTracesToolStripMenuItem.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
            this.accésAuFichierDeTracesToolStripMenuItem.Name = "accésAuFichierDeTracesToolStripMenuItem";
            this.accésAuFichierDeTracesToolStripMenuItem.Size = new System.Drawing.Size(159, 22);
            this.accésAuFichierDeTracesToolStripMenuItem.Text = "Fichiers de trace";
            this.accésAuFichierDeTracesToolStripMenuItem.Click += new System.EventHandler(this.AccésAuFichierDeTracesToolStripMenuItem_Click);
            // 
            // MENU_info
            // 
            this.MENU_info.Image = global::solonng_launcher.Properties.Resources.info;
            this.MENU_info.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
            this.MENU_info.Name = "MENU_info";
            this.MENU_info.Size = new System.Drawing.Size(159, 22);
            this.MENU_info.Text = "À propos";
            this.MENU_info.Click += new System.EventHandler(this.MENU_info_Click);
            // 
            // MENU_quit
            // 
            this.MENU_quit.Image = global::solonng_launcher.Properties.Resources.power;
            this.MENU_quit.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
            this.MENU_quit.Name = "MENU_quit";
            this.MENU_quit.Size = new System.Drawing.Size(159, 22);
            this.MENU_quit.Text = "Quitter";
            this.MENU_quit.Click += new System.EventHandler(this.MENU_quit_Click);
            // 
            // mainDataGridView
            // 
            this.mainDataGridView.AllowUserToAddRows = false;
            this.mainDataGridView.AllowUserToDeleteRows = false;
            this.mainDataGridView.AllowUserToResizeRows = false;
            this.mainDataGridView.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.mainDataGridView.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.mainDataGridView.BackgroundColor = System.Drawing.Color.White;
            this.mainDataGridView.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.mainDataGridView.CellBorderStyle = System.Windows.Forms.DataGridViewCellBorderStyle.None;
            this.mainDataGridView.ClipboardCopyMode = System.Windows.Forms.DataGridViewClipboardCopyMode.EnableWithoutHeaderText;
            this.mainDataGridView.ColumnHeadersBorderStyle = System.Windows.Forms.DataGridViewHeaderBorderStyle.None;
            dataGridViewCellStyle5.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle5.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(25)))), ((int)(((byte)(49)))), ((int)(((byte)(88)))));
            dataGridViewCellStyle5.Font = new System.Drawing.Font("Arial", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            dataGridViewCellStyle5.ForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle5.SelectionBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(25)))), ((int)(((byte)(49)))), ((int)(((byte)(88)))));
            dataGridViewCellStyle5.SelectionForeColor = System.Drawing.Color.White;
            dataGridViewCellStyle5.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.mainDataGridView.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle5;
            this.mainDataGridView.ColumnHeadersHeight = 40;
            this.mainDataGridView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.DisableResizing;
            this.mainDataGridView.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.FileName,
            this.Status,
            this.Action,
            this.DocumentId});
            dataGridViewCellStyle6.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle6.BackColor = System.Drawing.SystemColors.Window;
            dataGridViewCellStyle6.Font = new System.Drawing.Font("Arial", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            dataGridViewCellStyle6.ForeColor = System.Drawing.Color.Black;
            dataGridViewCellStyle6.SelectionBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(217)))), ((int)(((byte)(221)))), ((int)(((byte)(230)))));
            dataGridViewCellStyle6.SelectionForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(74)))), ((int)(((byte)(94)))), ((int)(((byte)(129)))));
            dataGridViewCellStyle6.WrapMode = System.Windows.Forms.DataGridViewTriState.False;
            this.mainDataGridView.DefaultCellStyle = dataGridViewCellStyle6;
            this.mainDataGridView.EnableHeadersVisualStyles = false;
            this.mainDataGridView.GridColor = System.Drawing.Color.White;
            this.mainDataGridView.Location = new System.Drawing.Point(61, 209);
            this.mainDataGridView.MultiSelect = false;
            this.mainDataGridView.Name = "mainDataGridView";
            this.mainDataGridView.ReadOnly = true;
            this.mainDataGridView.RowHeadersVisible = false;
            this.mainDataGridView.RowHeadersWidth = 102;
            this.mainDataGridView.RowTemplate.Height = 35;
            this.mainDataGridView.RowTemplate.ReadOnly = true;
            this.mainDataGridView.RowTemplate.Resizable = System.Windows.Forms.DataGridViewTriState.False;
            this.mainDataGridView.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.mainDataGridView.Size = new System.Drawing.Size(861, 241);
            this.mainDataGridView.TabIndex = 3;
            this.mainDataGridView.Visible = false;
            this.mainDataGridView.CellMouseDown += new System.Windows.Forms.DataGridViewCellMouseEventHandler(this.MainDataGridView_CellMouseDown);
            this.mainDataGridView.CellMouseEnter += new System.Windows.Forms.DataGridViewCellEventHandler(this.MainDataGridView_CellMouseEnter);
            this.mainDataGridView.CellMouseMove += new System.Windows.Forms.DataGridViewCellMouseEventHandler(this.MainDataGridView_CellMouseMove);
            this.mainDataGridView.SelectionChanged += new System.EventHandler(this.MainDataGridView_SelectionChanged);
            this.mainDataGridView.KeyDown += new System.Windows.Forms.KeyEventHandler(this.MainDataGridView_KeyDown);
            // 
            // FileName
            // 
            this.FileName.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.FileName.HeaderText = "Fichier";
            this.FileName.MinimumWidth = 12;
            this.FileName.Name = "FileName";
            this.FileName.ReadOnly = true;
            // 
            // Status
            // 
            this.Status.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Status.FillWeight = 106.2606F;
            this.Status.HeaderText = "Statut";
            this.Status.MinimumWidth = 12;
            this.Status.Name = "Status";
            this.Status.ReadOnly = true;
            this.Status.Width = 200;
            // 
            // Action
            // 
            this.Action.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.None;
            this.Action.FillWeight = 81.21828F;
            this.Action.HeaderText = "";
            this.Action.MinimumWidth = 12;
            this.Action.Name = "Action";
            this.Action.ReadOnly = true;
            this.Action.Resizable = System.Windows.Forms.DataGridViewTriState.False;
            this.Action.Width = 45;
            // 
            // DocumentId
            // 
            this.DocumentId.HeaderText = "DocumentId";
            this.DocumentId.MinimumWidth = 12;
            this.DocumentId.Name = "DocumentId";
            this.DocumentId.ReadOnly = true;
            this.DocumentId.Visible = false;
            // 
            // header
            // 
            this.header.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.header.Controls.Add(this.btn_document_Click);
            this.header.Controls.Add(this.lblAppTitle);
            this.header.Controls.Add(this.pictureBox1);
            this.header.Dock = System.Windows.Forms.DockStyle.Top;
            this.header.Location = new System.Drawing.Point(0, 0);
            this.header.Name = "header";
            this.header.Size = new System.Drawing.Size(984, 97);
            this.header.TabIndex = 1;
            this.header.TabStop = true;
            this.header.MouseClick += new System.Windows.Forms.MouseEventHandler(this.MainWindow_MouseClick);
            // 
            // btn_document_Click
            // 
            this.btn_document_Click.BackgroundImage = global::solonng_launcher.Properties.Resources.folder__1_;
            this.btn_document_Click.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
            this.btn_document_Click.FlatAppearance.BorderColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.btn_document_Click.FlatAppearance.BorderSize = 0;
            this.btn_document_Click.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btn_document_Click.Location = new System.Drawing.Point(867, 29);
            this.btn_document_Click.Name = "btn_document_Click";
            this.btn_document_Click.Size = new System.Drawing.Size(55, 44);
            this.btn_document_Click.TabIndex = 2;
            this.btn_document_Click.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.mainWindowToolTip.SetToolTip(this.btn_document_Click, "Ouvrir le dossier contenant les documents à éditer");
            this.btn_document_Click.UseVisualStyleBackColor = true;
            this.btn_document_Click.Click += new System.EventHandler(this.Btn_document_Click);
            // 
            // lblAppTitle
            // 
            this.lblAppTitle.AutoSize = true;
            this.lblAppTitle.Font = new System.Drawing.Font("Arial", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblAppTitle.ForeColor = System.Drawing.Color.White;
            this.lblAppTitle.Location = new System.Drawing.Point(119, 38);
            this.lblAppTitle.Name = "lblAppTitle";
            this.lblAppTitle.Size = new System.Drawing.Size(115, 22);
            this.lblAppTitle.TabIndex = 0;
            this.lblAppTitle.Text = "SOLON Edit";
            this.lblAppTitle.MouseClick += new System.Windows.Forms.MouseEventHandler(this.MainWindow_MouseClick);
            // 
            // lblSectionTitle
            // 
            this.lblSectionTitle.AutoSize = true;
            this.lblSectionTitle.Font = new System.Drawing.Font("Arial", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblSectionTitle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(74)))), ((int)(((byte)(94)))), ((int)(((byte)(129)))));
            this.lblSectionTitle.Location = new System.Drawing.Point(57, 147);
            this.lblSectionTitle.Name = "lblSectionTitle";
            this.lblSectionTitle.Size = new System.Drawing.Size(255, 24);
            this.lblSectionTitle.TabIndex = 3;
            this.lblSectionTitle.Text = "Fichiers en cours d\'édition";
            this.lblSectionTitle.MouseClick += new System.Windows.Forms.MouseEventHandler(this.MainWindow_MouseClick);
            // 
            // mainWindowToolTip
            // 
            this.mainWindowToolTip.ShowAlways = true;
            // 
            // lblMessage
            // 
            this.lblMessage.AutoSize = true;
            this.lblMessage.Font = new System.Drawing.Font("Arial", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblMessage.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(46)))), ((int)(((byte)(59)))), ((int)(((byte)(80)))));
            this.lblMessage.Location = new System.Drawing.Point(61, 209);
            this.lblMessage.Name = "lblMessage";
            this.lblMessage.Size = new System.Drawing.Size(238, 16);
            this.lblMessage.TabIndex = 18;
            this.lblMessage.Text = "Il n\'y a pas de fichiers en cours d\'édition";
            // 
            // contextMenuDataGridView
            // 
            this.contextMenuDataGridView.ImageScalingSize = new System.Drawing.Size(40, 40);
            this.contextMenuDataGridView.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.reUploadToolStripMenuItem,
            this.deleteToolStripMenuItem});
            this.contextMenuDataGridView.Name = "contextMenuDataGridView";
            this.contextMenuDataGridView.Size = new System.Drawing.Size(130, 48);
            // 
            // reUploadToolStripMenuItem
            // 
            this.reUploadToolStripMenuItem.Image = global::solonng_launcher.Properties.Resources.refresh;
            this.reUploadToolStripMenuItem.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
            this.reUploadToolStripMenuItem.Name = "reUploadToolStripMenuItem";
            this.reUploadToolStripMenuItem.Size = new System.Drawing.Size(129, 22);
            this.reUploadToolStripMenuItem.Text = "Re upload";
            this.reUploadToolStripMenuItem.Click += new System.EventHandler(this.ReUploadToolStripMenuItem_Click);
            // 
            // deleteToolStripMenuItem
            // 
            this.deleteToolStripMenuItem.Image = global::solonng_launcher.Properties.Resources.trashContextMenu;
            this.deleteToolStripMenuItem.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
            this.deleteToolStripMenuItem.Name = "deleteToolStripMenuItem";
            this.deleteToolStripMenuItem.Size = new System.Drawing.Size(129, 22);
            this.deleteToolStripMenuItem.Text = "Supprimer";
            this.deleteToolStripMenuItem.Click += new System.EventHandler(this.DeleteToolStripMenuItem_Click);
            // 
            // lblAssemblyVersion
            // 
            this.lblAssemblyVersion.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.lblAssemblyVersion.AutoSize = true;
            this.lblAssemblyVersion.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblAssemblyVersion.Location = new System.Drawing.Point(12, 420);
            this.lblAssemblyVersion.Name = "lblAssemblyVersion";
            this.lblAssemblyVersion.Size = new System.Drawing.Size(48, 15);
            this.lblAssemblyVersion.TabIndex = 19;
            this.lblAssemblyVersion.Text = "Version";
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.White;
            this.ClientSize = new System.Drawing.Size(984, 450);
            this.Controls.Add(this.lblAssemblyVersion);
            this.Controls.Add(this.lblMessage);
            this.Controls.Add(this.lblSectionTitle);
            this.Controls.Add(this.header);
            this.Controls.Add(this.mainDataGridView);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MinimumSize = new System.Drawing.Size(585, 400);
            this.Name = "MainWindow";
            this.Text = "Solon NG - éditeur";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form1_FormClosing);
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.MouseClick += new System.Windows.Forms.MouseEventHandler(this.MainWindow_MouseClick);
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.contextMenuStrip1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.mainDataGridView)).EndInit();
            this.header.ResumeLayout(false);
            this.header.PerformLayout();
            this.contextMenuDataGridView.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.NotifyIcon notifyIcon1;
        private System.Windows.Forms.ToolStripMenuItem MENU_info;
        private System.Windows.Forms.ToolStripMenuItem MENU_quit;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.DataGridView mainDataGridView;
        private System.Windows.Forms.Panel header;
        private System.Windows.Forms.Label lblAppTitle;
        private System.Windows.Forms.Label lblSectionTitle;
        private System.Windows.Forms.ToolTip mainWindowToolTip;
        private System.Windows.Forms.Label lblMessage;
        private System.Windows.Forms.ToolStripMenuItem accésAuFichierDeTracesToolStripMenuItem;
        private System.Windows.Forms.ContextMenuStrip contextMenuDataGridView;
        private System.Windows.Forms.ToolStripMenuItem reUploadToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem deleteToolStripMenuItem;
        private System.Windows.Forms.Button btn_document_Click;
        private System.Windows.Forms.DataGridViewTextBoxColumn FileName;
        private System.Windows.Forms.DataGridViewTextBoxColumn Status;
        private System.Windows.Forms.DataGridViewImageColumn Action;
        private System.Windows.Forms.DataGridViewTextBoxColumn DocumentId;
        private System.Windows.Forms.Label lblAssemblyVersion;
    }
}

