using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;

namespace EvaluatorMVC.Models
{
    public class CustomRoleProvider : RoleProvider
    {
        //http://stackoverflow.com/questions/20521260/custom-role-provider-could-not-find-stored-procedure-dbo-aspnet-checkschemavers
        public override string[] GetRolesForUser(string username)
        {
            using (var db = new ApplicationDbContext())
            {
                var user = db.Users.SingleOrDefault(x => x.UserName == username);
                if (user == null)
                    return new string[] { };
                return user.Roles == null ? new string[] { } :
                  user.Roles.Select(u => u.Role.Name).ToArray();
            }
        }


        public override bool IsUserInRole(string username, string roleName)
        {

            using (var db = new ApplicationDbContext())
            { 
                
                
            }

            return true;
        }



        public override void AddUsersToRoles(string[] usernames, string[] roleNames)
        {
           
        }

        public override string ApplicationName
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public override void CreateRole(string roleName)
        {
            throw new NotImplementedException();
        }

        public override bool DeleteRole(string roleName, bool throwOnPopulatedRole)
        {
            throw new NotImplementedException();
        }

        public override string[] FindUsersInRole(string roleName, string usernameToMatch)
        {
            throw new NotImplementedException();
        }

        public override string[] GetAllRoles()
        {
            throw new NotImplementedException();
        }

      

        public override string[] GetUsersInRole(string roleName)
        {
            throw new NotImplementedException();
        }

        public override void RemoveUsersFromRoles(string[] usernames, string[] roleNames)
        {
            throw new NotImplementedException();
        }

        public override bool RoleExists(string roleName)
        {
            throw new NotImplementedException();
        }
    }
}