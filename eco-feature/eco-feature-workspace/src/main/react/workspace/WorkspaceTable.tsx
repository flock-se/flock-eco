import React, {useEffect, useState} from 'react'

import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableHead from '@material-ui/core/TableHead'
import TableFooter from '@material-ui/core/TableFooter'
import TableRow from '@material-ui/core/TableRow'
import TablePagination from '@material-ui/core/TablePagination'
import gql from 'graphql-tag'
import {useQuery} from '@apollo/react-hooks'
import {TableSortLabel} from '@material-ui/core'
import {Direction, WorkspaceTableQuery, WorkspaceTableQuery_list, WorkspaceTableQueryVariables} from '../Apollo'
// @ts-ignore
import {AlignedLoader} from '@flock-eco/core/src/main/react/components/AlignedLoader'

interface Props {
  reload: boolean,
  size?: number,
  onRowClick(id:string): void
}

interface State {
  size: number,
  page: number,
  order: string,
  direction: 'asc' | 'desc'
}

export const QUERY = gql`
    query WorkspaceTableQuery($pageable: Pageable) {
        list: findWorkspaceAll(pageable:$pageable) {
            id,
            host,
            name,
        }
        count:countWorkspaceAll
    }`

export function WorkspaceTable({reload, size, onRowClick}:Props) {

  const [state, setState] = useState<State>({
    size: size || 10,
    page: 0,
    order: 'name',
    direction: 'asc',
  })

  const {data, error, loading, refetch} = useQuery<WorkspaceTableQuery, WorkspaceTableQueryVariables>(QUERY, {
    variables: {
      pageable: {
        page: state.page,
        size: state.size,
        sort: {
          order: state.order,
          direction: state.direction.toUpperCase() as Direction,
        },
      },
    },
  })

  useEffect(() => {
    refetch()
  }, [reload])

  const handleChangePage = (event: React.MouseEvent<HTMLButtonElement> | null, page: number) => {
    setState(prev => ({...prev, page}))
  }

  const handleRowClick = (workspace:WorkspaceTableQuery_list) => () => {
    onRowClick && onRowClick(workspace.id)
  }

  if(loading)
    return (<AlignedLoader height={250}/>)
  if (!data)
    return null

  const fields:{
    key: keyof WorkspaceTableQuery_list,
    label:string
  }[] = [
    {key: 'name', label: 'Name'},
    {key: 'host', label: 'Host'},
  ]

  const sortHandler = (key: string) => () => {
    if (state.order !== key) {
      setState(prev => ({...prev, order: key}))
    } else {
      setState(prev => ({...prev, direction: prev.direction !== 'asc' ? 'asc' : 'desc'}))
    }
  }

  return (
    <Table>
      <TableHead>
        <TableRow>
          {fields.map(field => (<TableCell key={field.key}>
            <TableSortLabel
              active={state.order === field.key}
              direction={state.order === field.key ? state.direction : 'asc'}
              onClick={sortHandler(field.key)}>
              {field.label}
            </TableSortLabel>
          </TableCell>))}
        </TableRow>
      </TableHead>
      <TableBody>
        {data.list.map(it => (
          <TableRow key={it.name} hover onClick={handleRowClick(it)}>
            {fields.map(field => (<TableCell>{it[field.key]}</TableCell>))}
          </TableRow>
        ))}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TablePagination
            count={data.count}
            rowsPerPage={state.size}
            page={state.page}
            rowsPerPageOptions={[]}
            onChangePage={handleChangePage}
          />
        </TableRow>
      </TableFooter>
    </Table>)
}
