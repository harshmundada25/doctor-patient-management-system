import { useEffect, useMemo, useState } from 'react';

export default function DataTable({ columns, rows, emptyText = 'No records found.', loading = false, pageSize = 5 }) {
  const [page, setPage] = useState(1);

  useEffect(() => {
    setPage(1);
  }, [rows, pageSize]);

  const totalPages = Math.max(1, Math.ceil(rows.length / pageSize));

  const visibleRows = useMemo(() => {
    const start = (page - 1) * pageSize;
    return rows.slice(start, start + pageSize);
  }, [rows, page, pageSize]);

  const paginationLabel = rows.length === 0 ? '0 of 0' : `${(page - 1) * pageSize + 1}-${Math.min(page * pageSize, rows.length)} of ${rows.length}`;

  return (
    <div className="overflow-hidden rounded-3xl border border-white/10 bg-white/5">
      <table className="min-w-full divide-y divide-white/10 text-left text-sm">
        <thead className="bg-white/5 text-slate-300">
          <tr>
            {columns.map((column) => (
              <th key={column.key} className="px-4 py-3 font-medium uppercase tracking-[0.2em]">
                {column.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-white/10">
          {loading ? (
            Array.from({ length: Math.min(pageSize, 4) }).map((_, index) => (
              <tr key={index} className="animate-pulse">
                {columns.map((column) => (
                  <td key={column.key} className="px-4 py-4">
                    <div className="h-4 rounded-full bg-white/10" />
                  </td>
                ))}
              </tr>
            ))
          ) : visibleRows.length === 0 ? (
            <tr>
              <td className="px-4 py-8 text-center text-slate-400" colSpan={columns.length}>
                {emptyText}
              </td>
            </tr>
          ) : (
            visibleRows.map((row, index) => (
              <tr key={row.id ?? index} className="hover:bg-white/5">
                {columns.map((column) => (
                  <td key={column.key} className="px-4 py-4 text-slate-200">
                    {column.render ? column.render(row) : row[column.key]}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>

      {!loading && rows.length > 0 && (
        <div className="flex flex-col gap-3 border-t border-white/10 px-4 py-3 text-sm text-slate-300 sm:flex-row sm:items-center sm:justify-between">
          <p>{paginationLabel}</p>
          <div className="flex items-center gap-2">
            <button className="btn-secondary px-3 py-2 disabled:cursor-not-allowed disabled:opacity-40" type="button" disabled={page === 1} onClick={() => setPage((current) => Math.max(1, current - 1))}>
              Previous
            </button>
            <span className="rounded-full border border-white/10 bg-white/5 px-3 py-2 text-xs uppercase tracking-[0.2em]">Page {page} of {totalPages}</span>
            <button className="btn-secondary px-3 py-2 disabled:cursor-not-allowed disabled:opacity-40" type="button" disabled={page === totalPages} onClick={() => setPage((current) => Math.min(totalPages, current + 1))}>
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  );
}