def call(String name = 'jenkins') {
    node {
        def namespace = "default"
        def app = 'master'
        def branch = 'dev'
        def tag = ''
        def gitURL = 'github.com/tamle60511/cicd.git'
        try {
            docker.withRegistry('', 'docker-hub') {
                stage('PRERUN') {
                    branch = sh(script: 'echo $branch', returnStdout: true).trim()
                    app = sh(script: 'echo $app', returnStdout:true).trim()
                    tag = sh(script: 'echo $tag', returnStdout:true).trim()
                    if (branch == "stg") {
                        namespace = "stg"
                    }
                }

                stage('CHECKOUT') {
                    withCredentials([usernamePassword(credentialsId: "github-tamle60511", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        git branch: "main", changelog: false, poll: false, url: "https://${GIT_USERNAME}:${GIT_PASSWORD}@${gitURL}"
                    }
                }
                
                stage('DEPLOY') {
                    echo "branch: ${branch} - app: ${app} - tag: ${tag}"
                    echo "Deploying ...."
                    sh "ls -la"
                    kubernetesDeploy(configs: "docs/apps/${branch}/${app}/${app}.yaml", kubeconfigId: "k8sconfig1")
                    // kubeconfig(credentialsId: 'k8sconfig', serverUrl: 'https://minikube:8443') {
                    //     sh "kubectl apply -f docs/apps/${branch}/${app}/${app}.yaml"                        
                    // }
                    echo "Finished Deploy"
                }
            }
        } catch (Exception err) {
            echo "err: ${err}"
            if (err.getMessage() != "FILEEXITED"){
                currentBuild.result = "FAILURE"
                print "Failed with reason: ${err}"
                throw err
            }
        }
    }
}
