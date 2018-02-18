SELECT * FROM USER 
WHERE 1=1
#if($id)
AND id = :id
#end
#if($name)
AND name = :name
#end