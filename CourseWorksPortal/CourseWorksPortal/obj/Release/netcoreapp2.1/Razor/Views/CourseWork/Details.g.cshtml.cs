#pragma checksum "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\CourseWork\Details.cshtml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "94a624063a297c36ae9e02048e1c532dbc2543d4"
// <auto-generated/>
#pragma warning disable 1591
[assembly: global::Microsoft.AspNetCore.Razor.Hosting.RazorCompiledItemAttribute(typeof(AspNetCore.Views_CourseWork_Details), @"mvc.1.0.view", @"/Views/CourseWork/Details.cshtml")]
[assembly:global::Microsoft.AspNetCore.Mvc.Razor.Compilation.RazorViewAttribute(@"/Views/CourseWork/Details.cshtml", typeof(AspNetCore.Views_CourseWork_Details))]
namespace AspNetCore
{
    #line hidden
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.AspNetCore.Mvc.Rendering;
    using Microsoft.AspNetCore.Mvc.ViewFeatures;
#line 1 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\_ViewImports.cshtml"
using CourseWorksPortal;

#line default
#line hidden
#line 2 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\_ViewImports.cshtml"
using CourseWorksPortal.Models;

#line default
#line hidden
    [global::Microsoft.AspNetCore.Razor.Hosting.RazorSourceChecksumAttribute(@"SHA1", @"94a624063a297c36ae9e02048e1c532dbc2543d4", @"/Views/CourseWork/Details.cshtml")]
    [global::Microsoft.AspNetCore.Razor.Hosting.RazorSourceChecksumAttribute(@"SHA1", @"5862bb10d6852a451dabeb59965264ad0875f29d", @"/Views/_ViewImports.cshtml")]
    public class Views_CourseWork_Details : global::Microsoft.AspNetCore.Mvc.Razor.RazorPage<CourseWorksPortal.Models.CourseWork>
    {
        private static readonly global::Microsoft.AspNetCore.Razor.TagHelpers.TagHelperAttribute __tagHelperAttribute_0 = new global::Microsoft.AspNetCore.Razor.TagHelpers.TagHelperAttribute("class", new global::Microsoft.AspNetCore.Html.HtmlString("form-control"), global::Microsoft.AspNetCore.Razor.TagHelpers.HtmlAttributeValueStyle.DoubleQuotes);
        #line hidden
        #pragma warning disable 0169
        private string __tagHelperStringValueBuffer;
        #pragma warning restore 0169
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperExecutionContext __tagHelperExecutionContext;
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperRunner __tagHelperRunner = new global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperRunner();
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager __backed__tagHelperScopeManager = null;
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager __tagHelperScopeManager
        {
            get
            {
                if (__backed__tagHelperScopeManager == null)
                {
                    __backed__tagHelperScopeManager = new global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager(StartTagHelperWritingScope, EndTagHelperWritingScope);
                }
                return __backed__tagHelperScopeManager;
            }
        }
        private global::Microsoft.AspNetCore.Mvc.TagHelpers.TextAreaTagHelper __Microsoft_AspNetCore_Mvc_TagHelpers_TextAreaTagHelper;
        #pragma warning disable 1998
        public async override global::System.Threading.Tasks.Task ExecuteAsync()
        {
#line 2 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\CourseWork\Details.cshtml"
  
    ViewBag.Title = "Details";

#line default
#line hidden
            BeginContext(79, 19, true);
            WriteLiteral("\n<h2>Course work: \"");
            EndContext();
            BeginContext(99, 10, false);
#line 6 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\CourseWork\Details.cshtml"
             Write(Model.Name);

#line default
#line hidden
            EndContext();
            BeginContext(109, 91, true);
            WriteLiteral("\"</h2>\n\n</br>\n\n<div>\n    <dl class=\"dl-horizontal\">\n        <dt>File name</dt>\n        <dd>");
            EndContext();
            BeginContext(201, 15, false);
#line 13 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\CourseWork\Details.cshtml"
       Write(Model.File_name);

#line default
#line hidden
            EndContext();
            BeginContext(216, 76, true);
            WriteLiteral("</dd>\n\n        </br>\n\n        <dt>Description</dt>\n        <dd>\n            ");
            EndContext();
            BeginContext(292, 73, false);
            __tagHelperExecutionContext = __tagHelperScopeManager.Begin("textarea", global::Microsoft.AspNetCore.Razor.TagHelpers.TagMode.StartTagAndEndTag, "65a40d8b22cb4a148e0a1c1752c2c6cf", async() => {
            }
            );
            __Microsoft_AspNetCore_Mvc_TagHelpers_TextAreaTagHelper = CreateTagHelper<global::Microsoft.AspNetCore.Mvc.TagHelpers.TextAreaTagHelper>();
            __tagHelperExecutionContext.Add(__Microsoft_AspNetCore_Mvc_TagHelpers_TextAreaTagHelper);
#line 19 "C:\Users\Admin\Desktop\Kursovoi\CourseWorksPortal\CourseWorksPortal\Views\CourseWork\Details.cshtml"
__Microsoft_AspNetCore_Mvc_TagHelpers_TextAreaTagHelper.For = ModelExpressionProvider.CreateModelExpression(ViewData, __model => __model.Description);

#line default
#line hidden
            __tagHelperExecutionContext.AddTagHelperAttribute("asp-for", __Microsoft_AspNetCore_Mvc_TagHelpers_TextAreaTagHelper.For, global::Microsoft.AspNetCore.Razor.TagHelpers.HtmlAttributeValueStyle.DoubleQuotes);
            __tagHelperExecutionContext.AddHtmlAttribute(__tagHelperAttribute_0);
            BeginWriteTagHelperAttribute();
            __tagHelperStringValueBuffer = EndWriteTagHelperAttribute();
            __tagHelperExecutionContext.AddHtmlAttribute("readonly", Html.Raw(__tagHelperStringValueBuffer), global::Microsoft.AspNetCore.Razor.TagHelpers.HtmlAttributeValueStyle.Minimized);
            await __tagHelperRunner.RunAsync(__tagHelperExecutionContext);
            if (!__tagHelperExecutionContext.Output.IsContentModified)
            {
                await __tagHelperExecutionContext.SetOutputContentAsync();
            }
            Write(__tagHelperExecutionContext.Output);
            __tagHelperExecutionContext = __tagHelperScopeManager.End();
            EndContext();
            BeginContext(365, 46, true);
            WriteLiteral("\n        </dd>\n\n        </br>\n    </dl>\n</div>");
            EndContext();
        }
        #pragma warning restore 1998
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.ViewFeatures.IModelExpressionProvider ModelExpressionProvider { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IUrlHelper Url { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IViewComponentHelper Component { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IJsonHelper Json { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IHtmlHelper<CourseWorksPortal.Models.CourseWork> Html { get; private set; }
    }
}
#pragma warning restore 1591
