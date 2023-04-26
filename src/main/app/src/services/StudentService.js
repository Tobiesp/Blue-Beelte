export async function getLastEventSnapshot(token) {
    try{
        const response = await fetch('/api/report/getLastEventSnapshot', {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': token}
      });
        return await response.json();
    }catch(error) {
        return [];
    }
}

export async function getRunningTotals(token) {
    try{
        const response = await fetch('/api/points/total/running/', {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': token}
      });
        return await response.json();
    }catch(error) {
        return [];
    }
}

//export async function createUser(data) {
//    const response = await fetch('/api/user', {
//        method: 'POST',
//        headers: {'Content-Type': 'application/json'},
//        body: JSON.stringify({user: data})
//      })
//    return await response.json();
//}