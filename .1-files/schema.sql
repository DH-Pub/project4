USE [TaskLogDB]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[team_members]') AND type in (N'U'))
ALTER TABLE [dbo].[team_members] DROP CONSTRAINT IF EXISTS [user_id]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[team_members]') AND type in (N'U'))
ALTER TABLE [dbo].[team_members] DROP CONSTRAINT IF EXISTS [team_id_fk]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[tbl_refreshtoken]') AND type in (N'U'))
ALTER TABLE [dbo].[tbl_refreshtoken] DROP CONSTRAINT IF EXISTS [FK_tbl_refreshtoken_users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[tasks]') AND type in (N'U'))
ALTER TABLE [dbo].[tasks] DROP CONSTRAINT IF EXISTS [task_project_id]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[submits]') AND type in (N'U'))
ALTER TABLE [dbo].[submits] DROP CONSTRAINT IF EXISTS [FK_submits_tasks]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[projects]') AND type in (N'U'))
ALTER TABLE [dbo].[projects] DROP CONSTRAINT IF EXISTS [FK_projects_teams]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[milestone]') AND type in (N'U'))
ALTER TABLE [dbo].[milestone] DROP CONSTRAINT IF EXISTS [milestone_project_id]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[documents]') AND type in (N'U'))
ALTER TABLE [dbo].[documents] DROP CONSTRAINT IF EXISTS [FK_documents_projects]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[comments]') AND type in (N'U'))
ALTER TABLE [dbo].[comments] DROP CONSTRAINT IF EXISTS [FK_comments_users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[comments]') AND type in (N'U'))
ALTER TABLE [dbo].[comments] DROP CONSTRAINT IF EXISTS [FK_comments_tasks]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[assignees]') AND type in (N'U'))
ALTER TABLE [dbo].[assignees] DROP CONSTRAINT IF EXISTS [assign_user_id]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[assignees]') AND type in (N'U'))
ALTER TABLE [dbo].[assignees] DROP CONSTRAINT IF EXISTS [assign_task_id]
GO
/****** Object:  Table [dbo].[users]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[users]
GO
/****** Object:  Table [dbo].[teams]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[teams]
GO
/****** Object:  Table [dbo].[team_members]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[team_members]
GO
/****** Object:  Table [dbo].[tbl_refreshtoken]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[tbl_refreshtoken]
GO
/****** Object:  Table [dbo].[tasks]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[tasks]
GO
/****** Object:  Table [dbo].[submits]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[submits]
GO
/****** Object:  Table [dbo].[projects]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[projects]
GO
/****** Object:  Table [dbo].[milestone]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[milestone]
GO
/****** Object:  Table [dbo].[documents]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[documents]
GO
/****** Object:  Table [dbo].[comments]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[comments]
GO
/****** Object:  Table [dbo].[assignees]    Script Date: 7/20/2023 11:47:17 AM ******/
DROP TABLE IF EXISTS [dbo].[assignees]
GO
/****** Object:  Table [dbo].[assignees]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[assignees](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[task_id] [varchar](40) NOT NULL,
	[user_id] [varchar](40) NULL,
	[added_by] [varchar](40) NULL,
	[added_at] [datetime] NULL,
 CONSTRAINT [PK_assignees] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[comments]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[comments](
	[id] [varchar](40) NOT NULL,
	[task_id] [varchar](40) NOT NULL,
	[comments] [text] NULL,
	[created_by] [varchar](40) NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_comments] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[documents]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[documents](
	[id] [varchar](40) NOT NULL,
	[description] [text] NULL,
	[files] [text] NULL,
	[project_id] [varchar](40) NOT NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_documents] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[milestone]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[milestone](
	[id] [varchar](40) NOT NULL,
	[name] [varchar](50) NULL,
	[description] [text] NULL,
	[from_date] [date] NULL,
	[to_date] [date] NULL,
	[projects_id] [varchar](40) NOT NULL,
 CONSTRAINT [PK_milestone] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[projects]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[projects](
	[id] [varchar](40) NOT NULL,
	[name] [varchar](100) NULL,
	[team_id] [varchar](40) NOT NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_projects] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[submits]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[submits](
	[id] [varchar](40) NOT NULL,
	[task_id] [varchar](40) NOT NULL,
	[note] [text] NULL,
	[attached] [text] NULL,
	[submitted_at] [datetime] NULL,
 CONSTRAINT [PK_submits] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tasks]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tasks](
	[id] [varchar](40) NOT NULL,
	[name] [varchar](200) NULL,
	[description] [text] NULL,
	[briefs] [text] NULL,
	[priority] [varchar](10) NULL,
	[category] [varchar](20) NULL,
	[estimated] [int] NULL,
	[actual_hours] [int] NULL,
	[start_date] [date] NULL,
	[end_date] [date] NULL,
	[due_date] [date] NULL,
	[files] [text] NULL,
	[status] [varchar](20) NULL,
	[status_update] [datetime] NULL,
	[project_id] [varchar](40) NOT NULL,
	[version] [varchar](50) NULL,
	[position] [int] NULL,
	[updated_at] [datetime] NULL,
	[created_at] [datetime] NULL,
	[parent_task] [varchar](40) NULL,
 CONSTRAINT [PK_tasks] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[tbl_refreshtoken]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbl_refreshtoken](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[expiry_date] [datetimeoffset](6) NOT NULL,
	[token] [varchar](255) NOT NULL,
	[user_id] [varchar](40) NULL,
 CONSTRAINT [PK_tbl_refreshtoken] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[team_members]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[team_members](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [varchar](40) NULL,
	[added_by] [varchar](40) NULL,
	[team_id] [varchar](40) NOT NULL,
	[role] [varchar](20) NULL,
	[added_at] [datetime] NULL,
 CONSTRAINT [PK_team_members] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[teams]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[teams](
	[id] [varchar](40) NOT NULL,
	[team_name] [varchar](50) NULL,
	[description] [text] NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_teams] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 7/20/2023 11:47:17 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[id] [varchar](40) NOT NULL,
	[email] [varchar](50) NOT NULL,
	[username] [varchar](50) NULL,
	[password] [varchar](250) NULL,
	[role] [varchar](20) NULL,
	[bio] [text] NULL,
	[pic] [varchar](250) NULL,
	[created_at] [datetime] NULL,
 CONSTRAINT [PK_users] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_users_email] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[assignees]  WITH CHECK ADD  CONSTRAINT [assign_task_id] FOREIGN KEY([task_id])
REFERENCES [dbo].[tasks] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[assignees] CHECK CONSTRAINT [assign_task_id]
GO
ALTER TABLE [dbo].[assignees]  WITH CHECK ADD  CONSTRAINT [assign_user_id] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[assignees] CHECK CONSTRAINT [assign_user_id]
GO
ALTER TABLE [dbo].[comments]  WITH CHECK ADD  CONSTRAINT [FK_comments_tasks] FOREIGN KEY([task_id])
REFERENCES [dbo].[tasks] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[comments] CHECK CONSTRAINT [FK_comments_tasks]
GO
ALTER TABLE [dbo].[comments]  WITH CHECK ADD  CONSTRAINT [FK_comments_users] FOREIGN KEY([created_by])
REFERENCES [dbo].[users] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[comments] CHECK CONSTRAINT [FK_comments_users]
GO
ALTER TABLE [dbo].[documents]  WITH CHECK ADD  CONSTRAINT [FK_documents_projects] FOREIGN KEY([project_id])
REFERENCES [dbo].[projects] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[documents] CHECK CONSTRAINT [FK_documents_projects]
GO
ALTER TABLE [dbo].[milestone]  WITH CHECK ADD  CONSTRAINT [milestone_project_id] FOREIGN KEY([projects_id])
REFERENCES [dbo].[projects] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[milestone] CHECK CONSTRAINT [milestone_project_id]
GO
ALTER TABLE [dbo].[projects]  WITH CHECK ADD  CONSTRAINT [FK_projects_teams] FOREIGN KEY([team_id])
REFERENCES [dbo].[teams] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[projects] CHECK CONSTRAINT [FK_projects_teams]
GO
ALTER TABLE [dbo].[submits]  WITH CHECK ADD  CONSTRAINT [FK_submits_tasks] FOREIGN KEY([task_id])
REFERENCES [dbo].[tasks] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[submits] CHECK CONSTRAINT [FK_submits_tasks]
GO
ALTER TABLE [dbo].[tasks]  WITH CHECK ADD  CONSTRAINT [task_project_id] FOREIGN KEY([project_id])
REFERENCES [dbo].[projects] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[tasks] CHECK CONSTRAINT [task_project_id]
GO
ALTER TABLE [dbo].[tbl_refreshtoken]  WITH CHECK ADD  CONSTRAINT [FK_tbl_refreshtoken_users] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[tbl_refreshtoken] CHECK CONSTRAINT [FK_tbl_refreshtoken_users]
GO
ALTER TABLE [dbo].[team_members]  WITH CHECK ADD  CONSTRAINT [team_id_fk] FOREIGN KEY([team_id])
REFERENCES [dbo].[teams] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[team_members] CHECK CONSTRAINT [team_id_fk]
GO
ALTER TABLE [dbo].[team_members]  WITH CHECK ADD  CONSTRAINT [user_id] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[team_members] CHECK CONSTRAINT [user_id]
GO
