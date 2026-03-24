def setupAnsible() {
    sh '''
    sudo -u ansible ${ANSIBLE_VENV}/bin/python -m pip install -r requirements.txt
    sudo -u ansible ${ANSIBLE_VENV}/bin/ansible-galaxy collection install bitwarden.secrets --upgrade
    sudo -u ansible ${ANSIBLE_VENV}/bin/ansible --version
    '''
}

def runPlaybook(Map envVars = [:], Map extraVars = [:]) {
    def exports = envVars.collect { k, v -> "export ${k}='${v}'" }.join('\n')
    def extraArgs = extraVars.collect { k, v -> "-e ${k}='${v}'" }.join(' ')
    sh """
    sudo -u ansible bash -c "
        ${exports}
        ${ANSIBLE_VENV}/bin/ansible-playbook -i ${INVENTORY} ${PLAYBOOK} ${extraArgs}
    "
    """
}

def notifyFailed() {
    emailext (
        subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        to: 'lab@sponar.de'
    )
}

def notifySuccess(String message = "Job completed successfully.") {
    emailext (
        subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>${message}</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        to: 'lab@sponar.de'
    )
}

return this
