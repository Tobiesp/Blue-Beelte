"use strict";(self.webpackChunkblue_beetle=self.webpackChunkblue_beetle||[]).push([[511],{4511:(k,p,a)=>{a.r(p),a.d(p,{AccountModule:()=>L});var o=a(433),u=a(6895),s=a(9230),e=a(8256),m=a(1953);let _=(()=>{class t{constructor(r,n){this.router=r,this.accountService=n,this.accountService.userValue&&this.router.navigate(["/"])}}return t.\u0275fac=function(r){return new(r||t)(e.Y36(s.F0),e.Y36(m.B))},t.\u0275cmp=e.Xpm({type:t,selectors:[["ng-component"]],decls:2,vars:0,consts:[[1,"container","col-md-6","offset-md-3","mt-5"]],template:function(r,n){1&r&&(e.TgZ(0,"div",0),e._UZ(1,"router-outlet"),e.qZA())},dependencies:[s.lC],encapsulation:2}),t})();var g=a(590);function Z(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Username is required"),e.qZA())}function b(t,i){if(1&t&&(e.TgZ(0,"div",12),e.YNc(1,Z,2,0,"div",13),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.username.errors.required)}}function h(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Password is required"),e.qZA())}function C(t,i){if(1&t&&(e.TgZ(0,"div",12),e.YNc(1,h,2,0,"div",13),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.password.errors.required)}}function q(t,i){1&t&&e._UZ(0,"span",14)}const v=function(t){return{"is-invalid":t}};function A(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"First Name is required"),e.qZA())}function N(t,i){if(1&t&&(e.TgZ(0,"div",14),e.YNc(1,A,2,0,"div",15),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.firstName.errors.required)}}function U(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Last Name is required"),e.qZA())}function J(t,i){if(1&t&&(e.TgZ(0,"div",14),e.YNc(1,U,2,0,"div",15),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.lastName.errors.required)}}function I(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Username is required"),e.qZA())}function Y(t,i){if(1&t&&(e.TgZ(0,"div",14),e.YNc(1,I,2,0,"div",15),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.username.errors.required)}}function x(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Password is required"),e.qZA())}function y(t,i){1&t&&(e.TgZ(0,"div"),e._uU(1,"Password must be at least 6 characters"),e.qZA())}function S(t,i){if(1&t&&(e.TgZ(0,"div",14),e.YNc(1,x,2,0,"div",15),e.YNc(2,y,2,0,"div",15),e.qZA()),2&t){const r=e.oxw();e.xp6(1),e.Q6J("ngIf",r.f.password.errors.required),e.xp6(1),e.Q6J("ngIf",r.f.password.errors.minlength)}}function w(t,i){1&t&&e._UZ(0,"span",16)}const l=function(t){return{"is-invalid":t}},Q=[{path:"",component:_,children:[{path:"login",component:(()=>{class t{constructor(r,n,d,c,f){this.formBuilder=r,this.route=n,this.router=d,this.accountService=c,this.alertService=f,this.loading=!1,this.submitted=!1}ngOnInit(){this.form=this.formBuilder.group({username:["",o.kI.required],password:["",o.kI.required]})}get f(){return this.form.controls}onSubmit(){this.submitted=!0,this.alertService.clear(),!this.form.invalid&&(this.loading=!0,this.accountService.login(this.f.username.value,this.f.password.value).pipe((0,g.P)()).subscribe({next:()=>{this.router.navigateByUrl(this.route.snapshot.queryParams.returnUrl||"/")},error:r=>{this.alertService.error(r),this.loading=!1}}))}}return t.\u0275fac=function(r){return new(r||t)(e.Y36(o.qu),e.Y36(s.gz),e.Y36(s.F0),e.Y36(m.B),e.Y36(m.c))},t.\u0275cmp=e.Xpm({type:t,selectors:[["ng-component"]],decls:21,vars:11,consts:[[1,"card"],[1,"card-header"],[1,"card-body"],[3,"formGroup","ngSubmit"],[1,"mb-3"],[1,"form-label"],["type","text","formControlName","username",1,"form-control",3,"ngClass"],["class","invalid-feedback",4,"ngIf"],["type","password","formControlName","password",1,"form-control",3,"ngClass"],[1,"btn","btn-primary",3,"disabled"],["class","spinner-border spinner-border-sm me-1",4,"ngIf"],["routerLink","../register",1,"btn","btn-link"],[1,"invalid-feedback"],[4,"ngIf"],[1,"spinner-border","spinner-border-sm","me-1"]],template:function(r,n){1&r&&(e.TgZ(0,"div",0)(1,"h4",1),e._uU(2,"Login"),e.qZA(),e.TgZ(3,"div",2)(4,"form",3),e.NdJ("ngSubmit",function(){return n.onSubmit()}),e.TgZ(5,"div",4)(6,"label",5),e._uU(7,"Username"),e.qZA(),e._UZ(8,"input",6),e.YNc(9,b,2,1,"div",7),e.qZA(),e.TgZ(10,"div",4)(11,"label",5),e._uU(12,"Password"),e.qZA(),e._UZ(13,"input",8),e.YNc(14,C,2,1,"div",7),e.qZA(),e.TgZ(15,"div")(16,"button",9),e.YNc(17,q,1,0,"span",10),e._uU(18," Login "),e.qZA(),e.TgZ(19,"a",11),e._uU(20,"Register"),e.qZA()()()()()),2&r&&(e.xp6(4),e.Q6J("formGroup",n.form),e.xp6(4),e.Q6J("ngClass",e.VKq(7,v,n.submitted&&n.f.username.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.username.errors),e.xp6(4),e.Q6J("ngClass",e.VKq(9,v,n.submitted&&n.f.password.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.password.errors),e.xp6(2),e.Q6J("disabled",n.loading),e.xp6(1),e.Q6J("ngIf",n.loading))},dependencies:[u.mk,u.O5,o._Y,o.Fj,o.JJ,o.JL,o.sg,o.u,s.yS],encapsulation:2}),t})()},{path:"register",component:(()=>{class t{constructor(r,n,d,c,f){this.formBuilder=r,this.route=n,this.router=d,this.accountService=c,this.alertService=f,this.loading=!1,this.submitted=!1}ngOnInit(){this.form=this.formBuilder.group({firstName:["",o.kI.required],lastName:["",o.kI.required],username:["",o.kI.required],password:["",[o.kI.required,o.kI.minLength(6)]]})}get f(){return this.form.controls}onSubmit(){this.submitted=!0,this.alertService.clear(),!this.form.invalid&&(this.loading=!0,this.accountService.register(this.form.value).pipe((0,g.P)()).subscribe({next:()=>{this.alertService.success("Registration successful",{keepAfterRouteChange:!0}),this.router.navigate(["../login"],{relativeTo:this.route})},error:r=>{this.alertService.error(r),this.loading=!1}}))}}return t.\u0275fac=function(r){return new(r||t)(e.Y36(o.qu),e.Y36(s.gz),e.Y36(s.F0),e.Y36(m.B),e.Y36(m.c))},t.\u0275cmp=e.Xpm({type:t,selectors:[["ng-component"]],decls:31,vars:19,consts:[[1,"card"],[1,"card-header"],[1,"card-body"],[3,"formGroup","ngSubmit"],[1,"mb-3"],[1,"form-label"],["type","text","formControlName","firstName",1,"form-control",3,"ngClass"],["class","invalid-feedback",4,"ngIf"],["type","text","formControlName","lastName",1,"form-control",3,"ngClass"],["type","text","formControlName","username",1,"form-control",3,"ngClass"],["type","password","formControlName","password",1,"form-control",3,"ngClass"],[1,"btn","btn-primary",3,"disabled"],["class","spinner-border spinner-border-sm me-1",4,"ngIf"],["routerLink","../login",1,"btn","btn-link"],[1,"invalid-feedback"],[4,"ngIf"],[1,"spinner-border","spinner-border-sm","me-1"]],template:function(r,n){1&r&&(e.TgZ(0,"div",0)(1,"h4",1),e._uU(2,"Register"),e.qZA(),e.TgZ(3,"div",2)(4,"form",3),e.NdJ("ngSubmit",function(){return n.onSubmit()}),e.TgZ(5,"div",4)(6,"label",5),e._uU(7,"First Name"),e.qZA(),e._UZ(8,"input",6),e.YNc(9,N,2,1,"div",7),e.qZA(),e.TgZ(10,"div",4)(11,"label",5),e._uU(12,"Last Name"),e.qZA(),e._UZ(13,"input",8),e.YNc(14,J,2,1,"div",7),e.qZA(),e.TgZ(15,"div",4)(16,"label",5),e._uU(17,"Username"),e.qZA(),e._UZ(18,"input",9),e.YNc(19,Y,2,1,"div",7),e.qZA(),e.TgZ(20,"div",4)(21,"label",5),e._uU(22,"Password"),e.qZA(),e._UZ(23,"input",10),e.YNc(24,S,3,2,"div",7),e.qZA(),e.TgZ(25,"div")(26,"button",11),e.YNc(27,w,1,0,"span",12),e._uU(28," Register "),e.qZA(),e.TgZ(29,"a",13),e._uU(30,"Cancel"),e.qZA()()()()()),2&r&&(e.xp6(4),e.Q6J("formGroup",n.form),e.xp6(4),e.Q6J("ngClass",e.VKq(11,l,n.submitted&&n.f.firstName.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.firstName.errors),e.xp6(4),e.Q6J("ngClass",e.VKq(13,l,n.submitted&&n.f.lastName.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.lastName.errors),e.xp6(4),e.Q6J("ngClass",e.VKq(15,l,n.submitted&&n.f.username.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.username.errors),e.xp6(4),e.Q6J("ngClass",e.VKq(17,l,n.submitted&&n.f.password.errors)),e.xp6(1),e.Q6J("ngIf",n.submitted&&n.f.password.errors),e.xp6(2),e.Q6J("disabled",n.loading),e.xp6(1),e.Q6J("ngIf",n.loading))},dependencies:[u.mk,u.O5,o._Y,o.Fj,o.JJ,o.JL,o.sg,o.u,s.yS],encapsulation:2}),t})()}]}];let R=(()=>{class t{}return t.\u0275fac=function(r){return new(r||t)},t.\u0275mod=e.oAB({type:t}),t.\u0275inj=e.cJS({imports:[s.Bz.forChild(Q),s.Bz]}),t})(),L=(()=>{class t{}return t.\u0275fac=function(r){return new(r||t)},t.\u0275mod=e.oAB({type:t}),t.\u0275inj=e.cJS({imports:[u.ez,o.UX,R]}),t})()}}]);