import React from 'react';
import {
  Filter,
  List,
  Datagrid,
  TextField,
  EditButton,
  DeleteButton,
  Edit,
  SimpleForm,
  TextInput,
  Create,
  required,
  minLength,
  maxLength,
  regex,
  TopToolbar,
  ListButton,
  SelectInput,
  email
} from 'react-admin';

const validateEmail = [required(), email()];

const validateUsername = [
  required(),
  minLength(3),
  maxLength(30),
  regex(/^[a-z0-9_-]+$/, 'Usuário inválido ou já em uso')
];

const validatePassword = [
  required(),
  minLength(3),
]

const UsersTitle = ({ record }) => {
  return <span>Usuários {record ? `"${record.username}"` : ''}</span>;
};

const UsersFilter = (props) => (
  <Filter {...props}>
    <TextInput label="Buscar" source="q" alwaysOn />
  </Filter>
);

const UsersActions = ({ basePath, data, resource }) => (
  <TopToolbar>
    <ListButton basePath={basePath} record={data} />
  </TopToolbar>
);


export const UsersList = props => (
  <List filters={<UsersFilter />} {...props}>
    <Datagrid rowClick="edit">
      <TextField source="username" />
      <TextField source="email" />
      <EditButton />
      <DeleteButton />
    </Datagrid>
  </List>
);

export const UsersCreate = props => (
  <Create {...props} actions={<UsersActions />}>
    <SimpleForm>
      <TextInput source="username" label="Usuário" validate={validateUsername} />
      <TextInput source="email" label="E-mail" validate={validateEmail} />
      <TextInput source="password" label="Senha" type="password" resettable validate={validatePassword} />
      <SelectInput source="roles" validate={required()} label="Perfil" optionValue="value" choices={[
      { id: 1, name: 'FINALIZADOR', value:"finalizador" },
      { id: 2, name: 'TRIADOR', value: "triador" },
      { id: 3, name: 'ADMINISTRADOR', value: "admin" },
    ]} />
    </SimpleForm>
  </Create>
);

export const UsersEdit = props => (
  <Edit title={<UsersTitle />} actions={<UsersActions />} {...props}>
    <SimpleForm>
      <TextInput source="id" disabled />
      <TextInput source="username" label="Usuário" validate={validateUsername} />
      <TextInput source="email" label="E-mail" validate={validateEmail} />
      <TextInput source="password" label="Senha" type="password" resettable validate={validatePassword} />
      <SelectInput source="roles" validate={required()} label="Perfil" optionValue="value" choices={[
        { id: 1, name: 'FINALIZADOR', value: [{id: 1, name: 'ROLE_FINALIZADOR'}]},
        { id: 2, name: 'TRIADOR', value: [{id: 2, name: 'ROLE_TRIADOR'}] },
        { id: 3, name: 'ADMINISTRADOR', value: [{id: 3, name: 'ROLE_ADMINISTRADOR'}] },
    ]} />
    </SimpleForm>
  </Edit>
);
