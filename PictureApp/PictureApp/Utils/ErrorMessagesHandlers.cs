using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ModelBinding;
using System.Collections.Generic;
using System.Linq;

namespace PictureApp.Utils
{
    public static class ErrorMessagesCreator
    {
        public static string CreateErrorMessagesForProperties(ModelStateDictionary modelState)
        {
            var errors = modelState.Select(x => new { Key = x.Key, Value = x.Value.Errors })
                .Where(y => y.Value.Count > 0)
                .ToList();

            var errorsMap = new Dictionary<string, List<string>>();

            foreach (var propErrors in errors)
            {
                var propertyErrors = new List<string>();
                foreach (var err in propErrors.Value)
                {
                    propertyErrors.Add(err.ErrorMessage);
                }

                if (string.Equals(propErrors.Key,string.Empty))
                    errorsMap.Add("server", propertyErrors);
                else
                    errorsMap.Add  (propErrors.Key, propertyErrors);

            }

            return errorsMap.First().Value.First();
          
        }
    }

    public class ValidationProblemDetails : ProblemDetails
    {
        public ICollection<ValidationError> ValidationErrors { get; set; }
    }

    public class ValidationError
    {
        public int Code { get; set; }
        public string Error { get; set; }
        public object Result {get; set;}
    }

}

