class RootController {
    sendWavFile(){
        let url = location.protocol + '//' + location.host + location.pathname + "app/receive/postAiffFile";
        let file = $("#sendWavChoose")[0].files[0];

        fetch(url, {
            method: 'POST',
            body: file,
            headers:{
                'Content-Type': 'audio/wav'
            }
        }).then(response => response.json())
            .then(response => {
                if(response.success){
                    console.log(response.id);
                    let newUrl = location.protocol + '//' + location.host + location.pathname + "app/receive/getAiffFile/" + response.id;

                    fetch(newUrl,{
                        method: 'GET',
                        headers:{
                            'Content-Type': 'application/json'
                        }
                    }).then(response => response.json())
                        .then(response => {
                            console.log(response);
                        }).catch(response => {
                        console.log(response)
                    });
                }
            }).catch(response => {
            console.log(response)
        })
    }

    sendAiffFile(){
        let url = location.protocol + '//' + location.host + location.pathname + "app/receive/postWaveFile";
        let file = $("#sendAiffChoose")[0].files[0];

        fetch(url, {
            method: 'POST',
            body: file,
            headers:{
                'Content-Type': 'audio/aiff'
            }
        }).then(response => response.json())
            .then(response => {
                if(response.success){
                    console.log(response.id);
                    let newUrl = location.protocol + '//' + location.host + location.pathname + "app/receive/getWaveFile/" + response.id;

                    fetch(newUrl,{
                        method: 'GET',
                        headers:{
                            'Content-Type': 'application/json'
                        }
                    }).then(response => response.json())
                        .then(response => {
                            console.log(response);
                        }).catch(response => {
                        console.log(response)
                    });
                }
            }).catch(response => {
            console.log(response)
        })
    }
}