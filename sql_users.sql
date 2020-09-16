INSERT INTO roles(name) VALUES('ROLE_FINALIZADOR');
INSERT INTO roles(name) VALUES('ROLE_TRIADOR');
INSERT INTO roles(name) VALUES('ROLE_ADMINISTRADOR');

INSERT INTO public.users (email,"password",username) VALUES 
('admin@admin.com','$2a$10$LH0reTc0ryE4PF3QfS9h9urlX7WrvvLmr23MX0P0T0J3axsUQi3d6','admin')
,('finalizador@user.com','$2a$10$2MfOFEjPFUS2sxMrcQPdY.bQoRiJwr/LcworHo5fJ5fltVSgIT.jS','finalizador')
,('triador@user.com','$2a$10$UP6OfFS2K95pVy8Uo.GlBOptaxzPXJFNd1GJMDAOJBie4q5coxMR.','triador');

INSERT INTO public.user_roles (user_id,role_id) VALUES 
(1,3)
,(3,1)
,(2,2);