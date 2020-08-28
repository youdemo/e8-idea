  select logid,workflowid,lastoperatedate,nodeid,cssl,( select workflowname from workflow_base where id=t.workflowid) as workflowname,(select nodename from workflow_nodebase where id=t.nodeid) as nodename  from(
   select max(logid) as logid,a.workflowid,a.lastoperatedate,b.nodeid,count(1) as cssl
  from workflow_requestbase a, workflow_requestlog b
 where a.requestid = b.requestid
   and a.currentnodetype >= 3
    and a.workflowid in (select distinct lcid from uf_workflow_csmt)
   and b.nodeid in (select distinct jdid from uf_workflow_csmt)
   and b.logtype in ('0','2','3')
   and f_ht_checkiscs(b.requestid,b.nodeid,b.operatedate,b.operatetime,b.logid)='1'
   group by a.workflowid,a.lastoperatedate,b.nodeid
   order by a.lastoperatedate asc,b.nodeid asc) t

