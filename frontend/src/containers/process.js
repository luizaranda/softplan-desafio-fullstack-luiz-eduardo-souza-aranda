import React, { Fragment } from 'react';
import {
  Filter,
  List,
  Datagrid,
  TextField,
  EditButton,
  Edit,
  SimpleForm,
  TextInput,
  Create,
  TopToolbar,
  ListButton,
  DateInput,
  BulkDeleteButton
} from 'react-admin';

const ProcessTitle = ({ record }) => {
  return <span>Processo {record ? `${record.number}` : ''}</span>;
};

const ProcessFilter = (props) => (
  <Filter {...props}>
    <TextInput label="Buscar" source="q" alwaysOn />
  </Filter>
);

const ProcessActions = ({ basePath, data, resource }) => (
  <TopToolbar>
    <ListButton basePath={basePath} record={data} />
  </TopToolbar>
);

const ProcessBulkActionButtons = props => {
//removendo as props que o component nao usa
const { permissions, hasList, hasEdit, hasShow, hasCreate, ...rest } = props
return (
    <Fragment>
      {permissions === "ROLE_ADMINISTRADOR" && (
        <span>
          <BulkDeleteButton {...rest} />
        </span>
      )}
  </Fragment>
)
};

export const ProcessList = props => {
  const {permissions} = props;
  return (
      <List {...props} filters={<ProcessFilter />} bulkActionButtons={<ProcessBulkActionButtons {...props}/>}>
        <Datagrid rowClick="">
          <TextField source="number" />
          <TextField source="author" />
          <TextField source="defendant" />
          <TextField source="distributionDate" />
          {(permissions === "ROLE_ADMINISTRADOR" || permissions === "ROLE_TRIADOR") && (
            <EditButton />
          )}
        </Datagrid>
      </List>
    )
  };

export const ProcessCreate = ({permissions, ...props}) => (
  <Create {...props} actions={<ProcessActions />}>
    <SimpleForm>
        <TextInput source="number" />
        <TextInput source="author" />
        <TextInput source="defendant" />
        <DateInput source="distributionDate" />
    </SimpleForm>
  </Create>
);

export const ProcessEdit = props => (
  <Edit title={<ProcessTitle />} actions={<ProcessActions />} {...props}>
   <SimpleForm>
        <TextField source="id" disabled/>
        <TextInput source="number" />
        <TextInput source="author" />
        <TextInput source="defendant" />
        <DateInput source="distributionDate" />
    </SimpleForm>
  </Edit>
);
