-- mysql

CREATE TABLE `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `GroupId` int(11) DEFAULT NULL,
  `Account` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Mail` varchar(50) DEFAULT NULL,
  `Phone` varchar(50) DEFAULT NULL,
  `QQ` varchar(50) DEFAULT NULL,
  `CreateDate` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ModifyDate` datetime DEFAULT NULL,
  `DeleteFlag` bit(1) DEFAULT NULL,
  PRIMARY KEY (`Id`)
)


-- sql server
CREATE TABLE [dbo].[User](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[GroupId] [bigint] NULL,
	[Account] [varbinary](50) NULL,
	[Password] [varbinary](50) NULL,
	[Name] [varbinary](50) NULL,
	[Mail] [varbinary](50) NULL,
	[Phone] [varbinary](50) NULL,
	[QQ] [varbinary](50) NULL,
	[CreateDate] [datetime] NULL,
	[ModifyDate] [datetime] NULL,
	[DeleteFlag] [bit] NULL,
	CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED ([Id] ASC)
)